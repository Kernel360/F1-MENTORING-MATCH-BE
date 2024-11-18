package com.biengual.userapi.question.infrastructure;

import static com.biengual.core.constant.RestrictionConstant.*;
import static com.biengual.core.response.error.code.ContentErrorCode.*;
import static com.biengual.core.response.error.code.QuestionErrorCode.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.bson.types.ObjectId;

import com.biengual.core.annotation.DataProvider;
import com.biengual.core.domain.document.content.ContentDocument;
import com.biengual.core.domain.document.content.script.Script;
import com.biengual.core.domain.document.question.QuestionDocument;
import com.biengual.core.domain.entity.content.ContentEntity;
import com.biengual.core.enums.ContentStatus;
import com.biengual.core.enums.QuestionType;
import com.biengual.core.response.error.exception.CommonException;
import com.biengual.userapi.ai.PerplexityApiClient;
import com.biengual.userapi.content.domain.ContentDocumentCustomRepository;
import com.biengual.userapi.content.domain.ContentDocumentRepository;
import com.biengual.userapi.content.domain.ContentRepository;
import com.biengual.userapi.question.domain.QuestionDocumentRepository;
import com.biengual.userapi.question.domain.QuestionStore;
import com.biengual.userapi.validator.QuestionValidator;

import kong.unirest.json.JSONArray;
import kong.unirest.json.JSONObject;
import lombok.RequiredArgsConstructor;

@DataProvider
@RequiredArgsConstructor
public class QuestionStoreImpl implements QuestionStore {
    private final QuestionDocumentRepository questionDocumentRepository;
    private final ContentRepository contentRepository;
    private final ContentDocumentRepository contentDocumentRepository;
    private final ContentDocumentCustomRepository contentDocumentCustomRepository;
    private final PerplexityApiClient apiClient;
    private final QuestionValidator quizValidator;

    // 문제 생성 메소드
    @Override
    public void createQuestion(Long contentId) {
        ContentDocument document = this.getContentDocument(contentId);
        List<String> scripts = document.getScripts()
            .stream()
            .map(Script::getEnScript)
            .toList();

        JSONObject questions = apiClient.getQuestionsByPerplexity(String.join(" ", scripts));

        List<String> questionIds = this.parseQuestions(questions);
        contentDocumentCustomRepository.updateQuestionIds(document.getId(), questionIds);
        this.updateContentInfo(contentId);
    }

    // Internal Methods ================================================================================================
    private ContentDocument getContentDocument(Long contentId) {
        ContentEntity content = getContentEntity(contentId);
        quizValidator.verifyQuestionAlreadyGenerated(content.getNumOfQuiz());
        return contentDocumentRepository.findById(new ObjectId(content.getMongoContentId()))
            .orElseThrow(() -> new CommonException(CONTENT_NOT_FOUND));
    }

    private ContentEntity getContentEntity(Long contentId) {
        return contentRepository.findById(contentId)
            .orElseThrow(() -> new CommonException(CONTENT_NOT_FOUND));
    }

    // 문제 생성 시 ContentStatus 변경 및 ContentEntity 의 문제 갯수를 업데이트하는 메소드
    private void updateContentInfo(Long contentId) {
        ContentEntity content = getContentEntity(contentId);
        content.updateStatus(ContentStatus.ACTIVATED);
        content.updateNumOfQuiz(NUM_OF_EACH_QUIZ * NUM_OF_QUIZ_TYPE);
    }

    // JSON 중에서 QuestionType 을 기준으로 파싱하는 메소드
    private List<String> parseQuestions(JSONObject response) {
        List<String> questionIds = new ArrayList<>();
        // Extract the content from the response
        for (QuestionType type : QuestionType.values()) {
            JSONArray questions = response.getJSONArray(type.name());
            if (questions == null) {
                throw new CommonException(QUESTION_JSON_PARSING_ERROR);
            }
             questionIds.addAll(this.saveToMongo(questions, type));
        }
        return questionIds;
    }

    // QuestionType 에 해당하는 JSONArray 를 MongoDB 에 저장하는 메소드
    private List<String> saveToMongo(JSONArray jsonArray, QuestionType type) {
        List<String> questionIds = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject matchItem = jsonArray.getJSONObject(i);

            String question = matchItem.getString("question");

            JSONArray exampleArray = matchItem.getJSONArray("examples");
            List<String> examples = IntStream.range(0, exampleArray.length())
                .mapToObj(exampleArray::getString)
                .collect(Collectors.toList());

            String answer = (type == QuestionType.ORDER)
                ? matchItem.getJSONArray("answer").toList().toString()
                : matchItem.get("answer").toString();

            String hint =  matchItem.getString("hint");

            QuestionDocument questionDocument = QuestionDocument.of(question, examples, answer, hint, type);
            questionDocumentRepository.save(questionDocument);
            questionIds.add(questionDocument.getId().toString());
        }

        return questionIds;
    }
}
