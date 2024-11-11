package com.biengual.userapi.learning.domain;


import com.biengual.userapi.dashboard.domain.DashboardInfo;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static com.biengual.core.domain.entity.content.QContentEntity.contentEntity;
import static com.biengual.core.domain.entity.scrap.QScrapEntity.scrapEntity;
import static com.biengual.core.domain.entity.userlearninghistory.QUserLearningHistoryEntity.userLearningHistoryEntity;

@Repository
@RequiredArgsConstructor
public class UserLearningHistoryCustomRepository {
    private final JPAQueryFactory queryFactory;

    // 해당 컨텐츠의 유저 학습률을 조회하기 위한 쿼리
    public Optional<Integer> findLearningRateByUserIdAndContentId(Long userId, Long contentId) {
        return Optional.ofNullable(
            queryFactory
            .select(userLearningHistoryEntity.learningRate)
            .from(userLearningHistoryEntity)
                .where(userLearningHistoryEntity.userId.eq(userId)
                    .and(userLearningHistoryEntity.contentId.eq(contentId)))
            .fetchOne()
        );
    }

    // TODO: 8개로 고정할 것인지?
    // 최근 학습 컨텐츠 8개를 학습률과 함께 조회하기 위한 쿼리
    public List<DashboardInfo.RecentLearning> findRecentLearningTop8ByUserId(Long userId) {
        return queryFactory
            .select(
                Projections.constructor(
                    DashboardInfo.RecentLearning.class,
                    contentEntity.id,
                    contentEntity.title,
                    contentEntity.thumbnailUrl,
                    contentEntity.contentType,
                    contentEntity.preScripts,
                    contentEntity.category.name,
                    contentEntity.videoDuration,
                    contentEntity.hits,
                    getIsScrappedByUserId(userId),
                    userLearningHistoryEntity.learningRate
                )
            )
            .from(userLearningHistoryEntity)
            .innerJoin(contentEntity)
            .on(userLearningHistoryEntity.contentId.eq(contentEntity.id))
            .where(userLearningHistoryEntity.userId.eq(userId))
            .limit(8)
            .orderBy(userLearningHistoryEntity.learningRate.desc())
            .fetch();
    }

    // isScrapped를 col로 받기 위한 확인하는 쿼리, 비로그인 상태 시 false 리턴
    private Expression<?> getIsScrappedByUserId(Long userId) {
        return userId != null ?
            JPAExpressions
                .selectOne()
                .from(scrapEntity)
                .where(scrapEntity.content.id.eq(contentEntity.id)
                    .and(scrapEntity.userId.eq(userId)))
                .exists()
            : Expressions.constant(false);
    }
}
