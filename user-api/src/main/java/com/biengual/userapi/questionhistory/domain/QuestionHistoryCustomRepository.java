package com.biengual.userapi.questionhistory.domain;

import static com.biengual.core.domain.entity.questionhistory.QQuestionHistoryEntity.*;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class QuestionHistoryCustomRepository {
    private final JPAQueryFactory queryFactory;

    public void updateExistingQuestionHistory(Long userId, String questionId, Boolean isCorrect) {
        queryFactory
            .update(questionHistoryEntity)
            .set(questionHistoryEntity.count, questionHistoryEntity.count.add(1))
            .set(questionHistoryEntity.finalTry, isCorrect)
            .set(questionHistoryEntity.updatedAt, LocalDateTime.now())
            .where(
                questionHistoryEntity.userId.eq(userId)
                    .and(questionHistoryEntity.questionId.eq(questionId))
                    .and(questionHistoryEntity.finalTry.eq(false))
            )
            .execute();
    }

    public List<String> findQuestionsCorrected(List<String> questionIds, Long userId) {
        return queryFactory
            .select(questionHistoryEntity.questionId)
            .from(questionHistoryEntity)
            .where(
                questionHistoryEntity.userId.eq(userId)
                    .and(questionHistoryEntity.questionId.in(questionIds))
                    .and(questionHistoryEntity.finalTry.eq(true))
            )
            .fetch();
    }
}
