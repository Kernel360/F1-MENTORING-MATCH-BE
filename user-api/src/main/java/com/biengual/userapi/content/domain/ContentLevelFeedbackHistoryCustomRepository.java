package com.biengual.userapi.content.domain;

import com.biengual.core.enums.ContentLevel;
import com.biengual.core.util.TimeRange;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.biengual.core.domain.entity.content.QContentLevelFeedbackHistoryEntity.contentLevelFeedbackHistoryEntity;

@Repository
@RequiredArgsConstructor
public class ContentLevelFeedbackHistoryCustomRepository {
    private final JPAQueryFactory queryFactory;

    // TimeRange 만큼 ContentId별로 ContentLevelFeedback을 집계하기 위한 쿼리
    public List<ContentInfo.AggregatedLevelFeedback> countContentLevelsGroupByContentIdInTimeRange(TimeRange timeRange) {
        return queryFactory
            .select(
                Projections.constructor(
                    ContentInfo.AggregatedLevelFeedback.class,
                    contentLevelFeedbackHistoryEntity.contentId,
                    this.countByContentLevel(ContentLevel.LOW),
                    this.countByContentLevel(ContentLevel.MEDIUM),
                    this.countByContentLevel(ContentLevel.HIGH),
                    contentLevelFeedbackHistoryEntity.contentLevel.count()
                )
            )
            .from(contentLevelFeedbackHistoryEntity)
            .where(this.isFeedbackTimeInTimeRange(timeRange))
            .groupBy(contentLevelFeedbackHistoryEntity.contentId)
            .fetch();
    }

    // 해당 컨텐츠에 대해 난이도를 피드백 했다면 ContentLevel을 아니라면 null을 반환하도록 하기 위한 쿼리
    public ContentLevel findContentLevelByUserIdAndContentId(Long userId, Long contentId) {
        return queryFactory
            .select(contentLevelFeedbackHistoryEntity.contentLevel)
            .from(contentLevelFeedbackHistoryEntity)
            .where(
                contentLevelFeedbackHistoryEntity.userId.eq(userId)
                    .and(contentLevelFeedbackHistoryEntity.contentId.eq(contentId))
            )
            .fetchOne();
    }

    // Internal Method =================================================================================================

    // ContentLevel 마다 집계하기 위한 조건
    private BooleanExpression isContentLevelEqual(ContentLevel contentLevel) {
        return contentLevelFeedbackHistoryEntity.contentLevel.eq(contentLevel);
    }

    // 조건에 따른 집계
    private NumberExpression<Long> countByBooleanExpression(BooleanExpression expression) {
        return new CaseBuilder()
            .when(expression)
            .then(1)
            .otherwise(0)
            .sum()
            .longValue();
    }

    // ContentLevel 마다 집계
    private NumberExpression<Long> countByContentLevel(ContentLevel contentLevel) {
        return this.countByBooleanExpression(this.isContentLevelEqual(contentLevel));
    }

    // 대상이 되는 FeedbackTime을 집계하기 위한 조건
    private BooleanExpression isFeedbackTimeInTimeRange(TimeRange timeRange) {
        return contentLevelFeedbackHistoryEntity.feedbackTime.goe(timeRange.start())
            .and(contentLevelFeedbackHistoryEntity.feedbackTime.lt(timeRange.end()));
    }
}
