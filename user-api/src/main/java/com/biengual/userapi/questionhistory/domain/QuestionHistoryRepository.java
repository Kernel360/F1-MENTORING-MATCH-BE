package com.biengual.userapi.questionhistory.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import com.biengual.core.domain.entity.questionhistory.QuestionHistoryEntity;

public interface QuestionHistoryRepository extends JpaRepository<QuestionHistoryEntity, Long> {
    boolean existsByUserIdAndQuestionIdAndFinalTryIsTrue(Long userId, String questionId);

    boolean existsByUserIdAndQuestionId(Long userId, String questionId);
}
