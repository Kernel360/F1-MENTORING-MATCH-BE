package com.biengual.userapi.missionhistory.domain;

import static com.biengual.core.domain.entity.missionhistory.QMissionHistoryEntity.*;

import java.util.List;

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
}
