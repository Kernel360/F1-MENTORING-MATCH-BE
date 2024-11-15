package com.biengual.userapi.questionhistory.domain;

import static com.biengual.core.domain.entity.questionhistory.QQuestionHistoryEntity.*;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.biengual.core.util.PeriodUtil;
import com.biengual.userapi.dashboard.domain.DashboardInfo;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.NumberExpression;
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

    public DashboardInfo.QuestionSummary findQuestionHistoryByUserIdInMonth(Long userId, YearMonth yearMonth) {
        LocalDateTime startOfMonth = PeriodUtil.getStartOfMonth(yearMonth);
        LocalDateTime endOfMonth = PeriodUtil.getEndOfMonth(yearMonth);

        return queryFactory
            .select(
                Projections.constructor(
                    DashboardInfo.QuestionSummary.class,
                    // 첫 시도 정답률 계산
                    getFirstTryCorrectRate(),
                    // 재시도 정답률 계산: 첫 시도가 틀린 경우에만 계산
                    getReTryCorrectRate()
                )
            )
            .from(questionHistoryEntity)
            .where(
                questionHistoryEntity.userId.eq(userId)
                    .and(questionHistoryEntity.updatedAt.between(startOfMonth, endOfMonth))
            )
            .groupBy(questionHistoryEntity.userId)
            .fetchOne();
    }

    // Internal Methods ================================================================================================
    private NumberExpression<Double> getFirstTryCorrectRate() {
        return questionHistoryEntity.firstTry
            .when(true).then(1.0).otherwise(0.0).sum()
            .divide(
                questionHistoryEntity.id.count().castToNum(Double.class)
            ).as("firstTryCorrectRate");
    }

    private NumberExpression<Double> getReTryCorrectRate() {
        return new CaseBuilder()
            .when(questionHistoryEntity.firstTry.eq(false)
                .and(questionHistoryEntity.finalTry.eq(true)))
            .then(1.0)
            .otherwise(0.0)
            .sum()
            .divide(
                questionHistoryEntity.firstTry
                    .when(false).then(questionHistoryEntity.count.subtract(1.0).castToNum(Double.class)).otherwise(0.0)
                    .sum()
                    .castToNum(Double.class)
            ).as("reTryCorrectRate");
    }
}