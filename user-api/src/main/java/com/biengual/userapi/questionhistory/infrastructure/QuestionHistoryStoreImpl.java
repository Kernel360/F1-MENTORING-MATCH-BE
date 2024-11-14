package com.biengual.userapi.questionhistory.infrastructure;

import com.biengual.core.annotation.DataProvider;
import com.biengual.core.domain.entity.questionhistory.QuestionHistoryEntity;
import com.biengual.userapi.questionhistory.domain.QuestionHistoryCustomRepository;
import com.biengual.userapi.questionhistory.domain.QuestionHistoryRepository;
import com.biengual.userapi.questionhistory.domain.QuestionHistoryStore;

import lombok.RequiredArgsConstructor;

@DataProvider
@RequiredArgsConstructor
public class QuestionHistoryStoreImpl implements QuestionHistoryStore {
    private final QuestionHistoryRepository questionHistoryRepository;
    private final QuestionHistoryCustomRepository questionHistoryCustomRepository;

    @Override
    public void recordQuestionHistory(Long userId, String questionId, Boolean isCorrect) {
        boolean exists = questionHistoryRepository.existsByUserIdAndQuestionId(userId, questionId);

        if (!exists) {
            // 처음 문제를 맞춘 경우 엔티티 생성 및 저장
            questionHistoryRepository.save(
                QuestionHistoryEntity.createQuestionHistory(userId, questionId, isCorrect)
            );
            return;
        }

        // 이미 존재하는 엔티티
        // count 와 finalTry 업데이트
        questionHistoryCustomRepository.updateExistingQuestionHistory(userId, questionId, isCorrect);
    }
}
