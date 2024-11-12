package com.biengual.userapi.learning.domain;

import com.biengual.core.util.PeriodUtil;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

import static com.biengual.core.domain.entity.learning.QLearningHistoryEntity.learningHistoryEntity;

@Repository
@RequiredArgsConstructor
public class LearningHistoryCustomRepository {
    private final JPAQueryFactory queryFactory;

    public boolean existsByUserIdAndContentIdInMonth(Long userId, Long contentId, LocalDateTime localDateTime) {

        LocalDateTime startOfMonth = PeriodUtil.getStartOfMonth(localDateTime);
        LocalDateTime endOfMonth = PeriodUtil.getEndOfMonth(localDateTime);

        return queryFactory
            .select(learningHistoryEntity.id)
            .from(learningHistoryEntity)
            .where(
                learningHistoryEntity.userId.eq(userId)
                    .and(learningHistoryEntity.contentId.eq(contentId))
                    .and(learningHistoryEntity.learningTime.between(startOfMonth, endOfMonth))
            )
            .fetchFirst() != null;
    }
}
