package com.biengual.userapi.learning.domain;

import static com.biengual.core.domain.entity.category.QCategoryEntity.*;
import static com.biengual.core.domain.entity.learning.QCategoryLearningHistoryEntity.*;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.biengual.core.util.PeriodUtil;
import com.biengual.userapi.dashboard.domain.DashboardInfo;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class CategoryLearningHistoryCustomRepository {
    private final JPAQueryFactory queryFactory;

    // 기간별 카테고리별 학습량을 조회하기 위한 쿼리
    public List<DashboardInfo.CategoryLearning> findCategoryLearningByUserIdInMonth(Long userId, YearMonth yearMonth) {
        LocalDateTime startOfMonth = PeriodUtil.getStartOfMonth(yearMonth);
        LocalDateTime endOfMonth = PeriodUtil.getEndOfMonth(yearMonth);

        return queryFactory
            .select(
                Projections.constructor(
                    DashboardInfo.CategoryLearning.class,
                    categoryLearningHistoryEntity.categoryId,
                    categoryEntity.name,
                    categoryLearningHistoryEntity.categoryId.count()
                )
            )
            .from(categoryLearningHistoryEntity)
            .leftJoin(categoryEntity)
            .on(categoryLearningHistoryEntity.categoryId.eq(categoryEntity.id))
            .where(
                categoryLearningHistoryEntity.userId.eq(userId)
                    .and(categoryLearningHistoryEntity.learningTime.between(startOfMonth, endOfMonth))
            )
            .groupBy(categoryLearningHistoryEntity.categoryId)
            .fetch();
    }

    // 기간별 전체 학습량을 조회하기 위한 쿼리
    public Long countCategoryLearningByUserIdInMonth(Long userId, YearMonth yearMonth) {
        LocalDateTime startOfMonth = PeriodUtil.getStartOfMonth(yearMonth);
        LocalDateTime endOfMonth = PeriodUtil.getEndOfMonth(yearMonth);

        return queryFactory
            .select(categoryLearningHistoryEntity.id.count())
            .from(categoryLearningHistoryEntity)
            .where(
                categoryLearningHistoryEntity.userId.eq(userId)
                    .and(categoryLearningHistoryEntity.learningTime.between(startOfMonth, endOfMonth))
            )
            .fetchOne();
    }
}
