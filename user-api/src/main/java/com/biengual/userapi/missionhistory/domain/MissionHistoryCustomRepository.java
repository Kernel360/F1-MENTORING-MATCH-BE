package com.biengual.userapi.missionhistory.domain;

import static com.biengual.core.domain.entity.missionhistory.QMissionHistoryEntity.*;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;

import com.biengual.core.util.PeriodUtil;
import com.biengual.userapi.dashboard.domain.DashboardInfo;
import com.querydsl.core.types.ConstructorExpression;
import org.springframework.stereotype.Repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class MissionHistoryCustomRepository {
    private final JPAQueryFactory queryFactory;

    public List<MissionHistoryInfo.RecentHistory> findRecentMissionHistory(Long userId) {
        return queryFactory
            .select(
                Projections.constructor(
                    MissionHistoryInfo.RecentHistory.class,
                    missionHistoryEntity.createdAt,
                    missionHistoryEntity.count
                )
            )
            .from(missionHistoryEntity)
            .where(missionHistoryEntity.userId.eq(userId))
            .orderBy(missionHistoryEntity.createdAt.desc())
            .fetch();
    }

    // 기간별 미션 내역을 조회하기 위한 쿼리
    public List<DashboardInfo.MissionHistory> findMissionHistoryByUserIdInMonth(Long userId, YearMonth yearMonth) {
        LocalDateTime startOfMonth = PeriodUtil.getStartOfMonth(yearMonth);
        LocalDateTime endOfMonth = PeriodUtil.getEndOfMonth(yearMonth);

        ConstructorExpression<DashboardInfo.MissionStatus> missionStatusProjection = Projections
            .constructor(
                DashboardInfo.MissionStatus.class,
                missionHistoryEntity.oneCount,
                missionHistoryEntity.bookmark,
                missionHistoryEntity.quiz,
                missionHistoryEntity.count
            );

        ConstructorExpression<DashboardInfo.MissionHistory> missionHistoryProjection = Projections
            .constructor(
                DashboardInfo.MissionHistory.class,
                missionHistoryEntity.createdAt,
                missionStatusProjection
            );

        return queryFactory
            .select(missionHistoryProjection)
            .from(missionHistoryEntity)
            .where(
                missionHistoryEntity.userId.eq(userId)
                    .and(missionHistoryEntity.createdAt.between(startOfMonth, endOfMonth))
            )
            .fetch();
    }
}
