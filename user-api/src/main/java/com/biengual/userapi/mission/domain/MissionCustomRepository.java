package com.biengual.userapi.mission.domain;

import static com.biengual.core.domain.entity.mission.QMissionEntity.*;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.querydsl.jpa.impl.JPAUpdateClause;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class MissionCustomRepository {
    private final JPAQueryFactory queryFactory;

    public void resetMission() {
        queryFactory
            .update(missionEntity)
            .set(missionEntity.oneContent, false)
            .set(missionEntity.bookmark, false)
            .set(missionEntity.quiz, false)
            .execute();
    }

    public Optional<MissionInfo.StatusInfo> findMissionStatusByUserId(Long userId) {
        NumberExpression<Integer> count = new CaseBuilder()
            .when(missionEntity.oneContent.isTrue()).then(1).otherwise(0)
            .add(new CaseBuilder().when(missionEntity.bookmark.isTrue()).then(1).otherwise(0))
            .add(new CaseBuilder().when(missionEntity.quiz.isTrue()).then(1).otherwise(0));

        return Optional.ofNullable(
            queryFactory
                .select(
                    Projections.constructor(
                        MissionInfo.StatusInfo.class,
                        missionEntity.oneContent,
                        missionEntity.bookmark,
                        missionEntity.quiz,
                        count
                    )
                )
                .from(missionEntity)
                .where(missionEntity.id.eq(userId))
                .fetchFirst()
        );
    }

    public void completeMission(MissionCommand.Update command) {
        JPAUpdateClause clause = queryFactory
            .update(missionEntity)
            .where(missionEntity.id.eq(command.missionId()));

        if (command.oneContent()) {
            clause.where(missionEntity.oneContent.isFalse())
                .set(missionEntity.oneContent, true);
        }
        if (command.bookmark()) {
            clause.where(missionEntity.bookmark.isFalse())
                .set(missionEntity.bookmark, true);
        }
        if (command.quiz()) {
            clause.where(missionEntity.quiz.isFalse())
                .set(missionEntity.quiz, true);
        }

        clause.execute();
    }
}
