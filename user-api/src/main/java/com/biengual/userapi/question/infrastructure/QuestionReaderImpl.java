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
        List<String> questionIds = this.getContentDocument(contentId).getQuestionIds();

        List<String> incorrectQuestionIds = this.getIncorrectQuestionIds(questionIds, userId);

        if (incorrectQuestionIds.isEmpty()) {
            return Collections.emptyList();
        }

        List<ObjectId> selectedQuestionIds = this.selectQuestionObjectIds(incorrectQuestionIds);

        return questionDocumentRepository.findByIdIn(selectedQuestionIds);
    }

    @Override
    public List<QuestionInfo.Detail> findCorrectedQuestionsByContentId(Long contentId, Long userId) {
        List<String> questionIds = this.getContentDocument(contentId).getQuestionIds();

        List<ObjectId> correctQuestionObjectIds = this.getCorrectQuestionObjectIds(questionIds, userId);

        if (correctQuestionObjectIds.isEmpty()) {
            return Collections.emptyList();
        }

        return questionDocumentRepository.findByIdIn(correctQuestionObjectIds);
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

    // 컨텐츠에 포함된 모든 문제 중 맞추지 못한 문제 id들을 얻는 메서드
    private List<String> getIncorrectQuestionIds(List<String> questionIds, Long userId) {
        List<String> questionDocumentIdsCorrected =
            questionHistoryCustomRepository.findQuestionsCorrected(questionIds, userId);

        return new ArrayList<>(
            questionIds
                .stream()
                .filter(quizId -> !questionDocumentIdsCorrected.contains(quizId))
                .toList()
        );
    }

    // 정답을 맞춘 적 없는 문제들 중 최대 MAX_QUIZ_SIZE 만큼 랜덤하게 ObjectId들을 얻는 메서드
    private List<ObjectId> selectQuestionObjectIds(List<String> questionDocumentIdsNotCorrected) {
        Collections.shuffle(questionDocumentIdsNotCorrected);

        return questionDocumentIdsNotCorrected
            .subList(0, Math.min(MAX_QUIZ_SIZE, questionDocumentIdsNotCorrected.size()))
            .stream()
            .map(ObjectId::new)
            .toList();
    }

    // 정답을 맞춘 문제 ObjectId들을 얻는 메서드
    private List<ObjectId> getCorrectQuestionObjectIds(List<String> questionIds, Long userId) {
        List<String> questionDocumentIdsCorrected =
            questionHistoryCustomRepository.findQuestionsCorrected(questionIds, userId);

        return questionDocumentIdsCorrected
            .stream()
            .map(ObjectId::new)
            .toList();
    }
}