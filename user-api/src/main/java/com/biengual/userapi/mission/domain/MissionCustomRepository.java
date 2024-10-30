package com.biengual.userapi.mission.domain;

import static com.biengual.core.domain.entity.mission.QMissionEntity.*;

import java.time.LocalDate;
import java.util.Objects;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class MissionCustomRepository {
    private final JPAQueryFactory queryFactory;

    public void resetMission(Long userId) {
        queryFactory
            .update(missionEntity)
            .set(missionEntity.oneContent, false)
            .set(missionEntity.memo, false)
            .set(missionEntity.quiz, false)
            .where(missionEntity.id.eq(userId))
            .execute();

    }

    public boolean checkMissionDate(Long userId) {
        return !Objects.equals(
            queryFactory
                .select(missionEntity.missionDate)
                .from(missionEntity)
                .where(missionEntity.id.eq(userId))
                .fetchFirst(), LocalDate.now()
        );
    }

    public Optional<MissionInfo.StatusInfo> findMissionStatusByUserId(Long userId) {
        return Optional.ofNullable(
            queryFactory
                .select(
                    Projections.constructor(
                        MissionInfo.StatusInfo.class,
                        missionEntity.oneContent,
                        missionEntity.memo,
                        missionEntity.quiz
                    )
                )
                .from(missionEntity)
                .where(missionEntity.id.eq(userId))
                .fetchFirst()
        );
    }
}
