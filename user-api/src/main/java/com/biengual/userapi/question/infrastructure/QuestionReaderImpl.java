package com.biengual.userapi.question.infrastructure;

import static com.biengual.core.constant.RestrictionConstant.*;
import static com.biengual.core.response.error.code.ContentErrorCode.*;
import static com.biengual.core.response.error.code.QuestionErrorCode.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.bson.types.ObjectId;

import com.biengual.core.annotation.DataProvider;
import com.biengual.core.domain.document.content.ContentDocument;
import com.biengual.core.domain.document.question.QuestionDocument;
import com.biengual.core.domain.entity.content.ContentEntity;
import com.biengual.core.enums.ContentStatus;
import com.biengual.core.response.error.exception.CommonException;
import com.biengual.userapi.content.domain.ContentDocumentRepository;
import com.biengual.userapi.content.domain.ContentRepository;
import com.biengual.userapi.question.domain.QuestionDocumentRepository;
import com.biengual.userapi.question.domain.QuestionInfo;
import com.biengual.userapi.question.domain.QuestionReader;
import com.biengual.userapi.questionhistory.domain.QuestionHistoryCustomRepository;

import lombok.RequiredArgsConstructor;

@DataProvider
@RequiredArgsConstructor
public class QuestionReaderImpl implements QuestionReader {
    private final QuestionDocumentRepository questionDocumentRepository;
    private final ContentRepository contentRepository;
    private final ContentDocumentRepository contentDocumentRepository;
    private final QuestionHistoryCustomRepository questionHistoryCustomRepository;

    @Override
    public List<QuestionInfo.Detail> findQuestionsByContentId(Long contentId, Long userId) {
        ContentDocument contentDocument = this.getContentDocument(contentId);

        // 컨텐츠에 포함된 모든 문제 중 맞추지 못한 문제 id
        List<String> questionDocumentIdsCorrected =
            questionHistoryCustomRepository.findQuestionsCorrected(contentDocument.getQuestionIds(), userId);

        List<String> questionDocumentIdsNotCorrected = new ArrayList<>(
            contentDocument.getQuestionIds()
                .stream()
                .filter(quizId -> !questionDocumentIdsCorrected.contains(quizId))
                .toList()
        );

        if (questionDocumentIdsNotCorrected.isEmpty()) {
            throw new CommonException(QUESTION_ALL_CORRECTED);
        }

        // 랜덤 셔플
        Collections.shuffle(questionDocumentIdsNotCorrected);

        // 정해진 문제 개수만큼 리턴
        // 만약 정해진 개수보다 맞추지 못한 문제가 적으면 리턴하는 문제 갯수는 MAX_QUIZ_SIZE보다 작음
        List<QuestionInfo.Detail> questions = new ArrayList<>();
        for (String questionDocumentId : questionDocumentIdsNotCorrected) {
            if (questions.size() == MAX_QUIZ_SIZE) {
                break;
            }

            QuestionDocument questionDocument = questionDocumentRepository.findById(new ObjectId(questionDocumentId))
                .orElseThrow(() -> new CommonException(QUESTION_NOT_FOUND));
            questions.add(
                QuestionInfo.Detail.of(questionDocument)
            );
        }

        return questions;
    }

    @Override
    public List<QuestionInfo.Detail> findCorrectedQuestionsByContentId(Long contentId, Long userId) {
        List<String> questionIds = this.getContentDocument(contentId).getQuestionIds();

        return questionHistoryCustomRepository.findQuestionsCorrected(questionIds, userId)
            .stream()
            .map(questionId -> QuestionInfo.Detail.of(this.findQuestionByQuestionId(questionId)))
            .toList();
    }

    @Override
    public QuestionDocument findQuestionByQuestionId(String questionId) {
        return questionDocumentRepository.findById(new ObjectId(questionId))
            .orElseThrow(() -> new CommonException(QUESTION_NOT_FOUND));
    }

    @Override
    public QuestionInfo.Hint findHintOfQuestion(String questionId) {
        return QuestionInfo.Hint.of(this.findQuestionByQuestionId(questionId).getHint());
    }

    // Internal Methods ------------------------------------------------------------------------------------------------
    private ContentEntity getContentEntity(Long contentId) {
        ContentEntity content = contentRepository.findById(contentId)
            .orElseThrow(() -> new CommonException(CONTENT_NOT_FOUND));

        if (!content.getContentStatus().equals(ContentStatus.ACTIVATED)) {
            throw new CommonException(CONTENT_IS_DEACTIVATED);
        }

        return content;
    }

    private ContentDocument getContentDocument(Long contentId) {
        ContentEntity content = getContentEntity(contentId);

        return contentDocumentRepository.findById(new ObjectId(content.getMongoContentId()))
            .orElseThrow(() -> new CommonException(CONTENT_NOT_FOUND));
    }
}