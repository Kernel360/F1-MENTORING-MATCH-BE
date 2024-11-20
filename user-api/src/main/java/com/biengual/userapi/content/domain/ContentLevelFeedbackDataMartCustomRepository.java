package com.biengual.userapi.content.domain;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import static com.biengual.core.domain.entity.content.QContentLevelFeedbackDataMart.contentLevelFeedbackDataMart;

@Repository
@RequiredArgsConstructor
public class ContentLevelFeedbackDataMartCustomRepository {
    private final JPAQueryFactory queryFactory;

    // 기존 집계 데이터에 추가로 집계된 ContentLevelFeedback 데이터를 더하기 위한 쿼리
    public void updateByAggregatedLevelFeedbackInfo(ContentInfo.AggregatedLevelFeedback info) {
        queryFactory
            .update(contentLevelFeedbackDataMart)
            .set(
                contentLevelFeedbackDataMart.levelHighCount,
                contentLevelFeedbackDataMart.levelHighCount.add(info.levelHighCount())
            )
            .set(
                contentLevelFeedbackDataMart.levelMediumCount,
                contentLevelFeedbackDataMart.levelMediumCount.add(info.levelMediumCount())
            )
            .set(
                contentLevelFeedbackDataMart.levelLowCount,
                contentLevelFeedbackDataMart.levelLowCount.add(info.levelLowCount())
            )
            .set(
                contentLevelFeedbackDataMart.feedbackTotalCount,
                contentLevelFeedbackDataMart.feedbackTotalCount.add(info.feedbackTotalCount())
            )
            .where(contentLevelFeedbackDataMart.contentId.eq(info.contentId()))
            .execute();
    }
}
