package com.biengual.userapi.questionhistory.domain;

import static com.biengual.core.domain.entity.questionhistory.QQuestionHistoryEntity.*;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.querydsl.core.types.dsl.CaseBuilder;
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
            .set(questionHistoryEntity.finalTry,
                new CaseBuilder()
                    .when(questionHistoryEntity.finalTry.eq(false))
                    .then(isCorrect)
                    .otherwise(questionHistoryEntity.finalTry)
            )
            .where(
                questionHistoryEntity.userId.eq(userId)
                    .and(questionHistoryEntity.questionId.eq(questionId))
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
