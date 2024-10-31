package com.biengual.core.domain.entity.missionhistory;

import org.apache.commons.lang3.BooleanUtils;
import org.hibernate.annotations.DynamicUpdate;

import com.biengual.core.domain.entity.BaseEntity;
import com.biengual.core.domain.entity.mission.MissionEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "mission_history")
@Getter
@DynamicUpdate
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MissionHistoryEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, columnDefinition = "bigint")
    private Long userId;

    @Column(nullable = false, columnDefinition = "int")
    private Integer count;

    @Column(name = "one_count", nullable = false)
    private boolean oneCount;

    @Column(nullable = false)
    private boolean bookmark;

    @Column(nullable = false)
    private Boolean quiz;

    @Builder
    public MissionHistoryEntity(MissionEntity mission) {
        this.userId = mission.getUserId();
        this.oneCount = mission.isOneContent();
        this.bookmark = mission.isBookmark();
        this.quiz = mission.isQuiz();
        this.count = BooleanUtils.toInteger(mission.isOneContent())
            + BooleanUtils.toInteger(mission.isBookmark())
            + BooleanUtils.toInteger(mission.isQuiz());
    }

    public static MissionHistoryEntity createByMissionEntity(MissionEntity mission) {
        return MissionHistoryEntity.builder()
            .mission(mission)
            .build();
    }

}
