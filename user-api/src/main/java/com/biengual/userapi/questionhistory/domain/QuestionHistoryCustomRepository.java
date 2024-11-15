package com.biengual.userapi.questionhistory.domain;

import static com.biengual.core.domain.entity.questionhistory.QQuestionHistoryEntity.*;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.biengual.core.util.PeriodUtil;
import com.biengual.userapi.dashboard.domain.DashboardInfo;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.Expressions;
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

    public List<DashboardInfo.QuestionSummary> findQuestionHistoryByUserIdLastFiveWeeks(
        Long userId, LocalDate currentDate
    ) {
        LocalDate fiveWeeksAgo = PeriodUtil.getFewWeeksAgo(currentDate, 4, DayOfWeek.MONDAY);

        List<DashboardInfo.QuestionSummary> result = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            LocalDate weekStart = fiveWeeksAgo.plusWeeks(i);
            LocalDate weekEnd = weekStart.plusWeeks(1);

            DashboardInfo.QuestionSummary summary = queryFactory
                .select(
                    Projections.constructor(
                        DashboardInfo.QuestionSummary.class,
                        Expressions.constant(weekStart),
                        Expressions.constant(i + 1),
                        getFirstTryCorrect(),
                        getReTryCorrect(),
                        getTotalFirstTry(),
                        getTotalReTry()
                    )
                )
                .from(questionHistoryEntity)
                .where(
                    questionHistoryEntity.userId.eq(userId)
                        .and(questionHistoryEntity.updatedAt.between(weekStart.atStartOfDay(), weekEnd.atStartOfDay()))
                )
                .fetchOne();

            if (summary == null) {
                summary = DashboardInfo.QuestionSummary.of(
                    weekStart, i + 1, 0, 0, 0, 0
                );
            }

            result.add(summary);
        }

        return result;
    }

    // Internal Methods ================================================================================================
    private NumberExpression<Integer> getFirstTryCorrect() {
        return questionHistoryEntity.firstTry
            .when(true)
            .then(1)
            .otherwise(0)
            .sum()
            .coalesce(0);
    }

    private NumberExpression<Integer> getTotalFirstTry() {
        return questionHistoryEntity.id.count()
            .castToNum(Integer.class)
            .coalesce(0);
    }

    private NumberExpression<Integer> getReTryCorrect() {
        return new CaseBuilder()
            .when(questionHistoryEntity.firstTry.eq(false)
                .and(questionHistoryEntity.finalTry.eq(true)))
            .then(1)
            .otherwise(0)
            .sum()
            .coalesce(0);
    }

    private NumberExpression<Integer> getTotalReTry() {
        return questionHistoryEntity.firstTry
            .when(false).then(questionHistoryEntity.count.subtract(1).castToNum(Integer.class)).otherwise(0)
            .sum()
            .castToNum(Integer.class)
            .coalesce(0);
    }
}