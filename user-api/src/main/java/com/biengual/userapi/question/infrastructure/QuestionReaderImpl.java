package com.biengual.userapi.question.infrastructure;

import static com.biengual.core.response.error.code.ContentErrorCode.*;
import static com.biengual.core.response.error.code.QuestionErrorCode.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.bson.types.ObjectId;

import com.biengual.core.annotation.DataProvider;
import com.biengual.core.domain.document.content.ContentDocument;
import com.biengual.core.domain.document.question.QuestionDocument;
import com.biengual.core.domain.entity.content.ContentEntity;
import com.biengual.core.enums.ContentStatus;
import com.biengual.core.enums.QuestionType;
import com.biengual.core.response.error.exception.CommonException;
import com.biengual.userapi.content.domain.ContentCustomRepository;
import com.biengual.userapi.content.domain.ContentDocumentRepository;
import com.biengual.userapi.content.domain.ContentRepository;
import com.biengual.userapi.question.domain.QuestionCommand;
import com.biengual.userapi.question.domain.QuestionDocumentRepository;
import com.biengual.userapi.question.domain.QuestionInfo;
import com.biengual.userapi.question.domain.QuestionReader;

import lombok.RequiredArgsConstructor;

@DataProvider
@RequiredArgsConstructor
public class QuestionReaderImpl implements QuestionReader {
    private final QuestionDocumentRepository questionDocumentRepository;
    private final ContentRepository contentRepository;
    private final ContentDocumentRepository contentDocumentRepository;
    private final ContentCustomRepository contentCustomRepository;

    @Override
    public List<QuestionInfo.Detail> getQuestions(Long contentId) {

        ContentDocument contentDocument = this.getContentDocument(contentId);
        List<String> questionDocumentIds = contentDocument.getQuestionIds();

        if(questionDocumentIds.isEmpty()) {
            throw new CommonException(QUESTION_NOT_FOUND);
        }

        List<QuestionInfo.Detail> questions = new ArrayList<>();

        for (String questionDocumentId : questionDocumentIds) {
            QuestionDocument questionDocument = questionDocumentRepository.findById(new ObjectId(questionDocumentId))
                .orElseThrow(() -> new CommonException(QUESTION_NOT_FOUND));
            questions.add(
                QuestionInfo.Detail.builder()
                    .question(questionDocument.getQuestion())
                    .questionId(questionDocumentId)
                    .examples(questionDocument.getExamples())
                    .type(questionDocument.getType())
                    .build()
            );
        }
        return questions;
    }

    @Override
    public boolean verifyAnswer(QuestionCommand.Verify command) {
        QuestionDocument questionDocument = this.getQuestionDocument(command.questionId());
        String answer = (questionDocument.getType() == QuestionType.ORDER)
            ? this.parseAnswerOfOrder(command.answer())
            : command.answer();

        return Objects.equals(questionDocument.getAnswer(), answer);
    }

    // Internal Methods ------------------------------------------------------------------------------------------------
    private String parseAnswerOfOrder(String answer) {
        String[] splitAnswer = answer.split(" ");
        StringBuilder formattedAnswer = new StringBuilder("[");
        for (int i = 0; i < splitAnswer.length; i++) {
            formattedAnswer.append(splitAnswer[i]);
            if (i < splitAnswer.length - 1) {
                formattedAnswer.append(", ");
            }
        }
        return formattedAnswer.append("]").toString();
    }

    private ContentEntity getContentEntity(Long contentId) {
        ContentEntity content = contentRepository.findById(contentId)
            .orElseThrow(() -> new CommonException(CONTENT_NOT_FOUND));

        if(!content.getContentStatus().equals(ContentStatus.ACTIVATED)){
            throw new CommonException(CONTENT_IS_DEACTIVATED);
        }

        return content;
    }

    private ContentDocument getContentDocument(Long contentId) {
        ContentEntity content = getContentEntity(contentId);

        return contentDocumentRepository.findById(new ObjectId(content.getMongoContentId()))
            .orElseThrow(() -> new CommonException(CONTENT_NOT_FOUND));
    }

    private QuestionDocument getQuestionDocument(String questionID) {
        return questionDocumentRepository.findById(new ObjectId(questionID))
            .orElseThrow(() -> new CommonException(QUESTION_NOT_FOUND));
    }
}
