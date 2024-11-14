package com.biengual.userapi.pointhistory.domain;

import com.biengual.core.domain.entity.pointhistory.PointHistoryEntity;
import com.biengual.core.util.PeriodUtil;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;

import static com.biengual.core.domain.entity.pointhistory.QPointHistoryEntity.pointHistoryEntity;

@Repository
@RequiredArgsConstructor
public class PointHistoryCustomRepository {
    private final JPAQueryFactory queryFactory;

    // 기간별 유저의 모든 포인트 내역을 조회하기 위한 쿼리
    public List<PointHistoryEntity> findPointHistoryByUserIdInMonth(Long userId, YearMonth yearMonth) {
        LocalDateTime startOfMonth = PeriodUtil.getStartOfMonth(yearMonth);
        LocalDateTime endOfMonth = PeriodUtil.getEndOfMonth(yearMonth);

        return queryFactory
            .selectFrom(pointHistoryEntity)
            .where(
                pointHistoryEntity.user.id.eq(userId)
                    .and(pointHistoryEntity.createdAt.between(startOfMonth, endOfMonth))
            )
            .orderBy(pointHistoryEntity.createdAt.desc())
            .fetch();
    }
}
