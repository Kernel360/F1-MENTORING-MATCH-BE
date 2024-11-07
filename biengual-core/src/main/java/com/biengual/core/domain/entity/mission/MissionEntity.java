package com.biengual.core.domain.entity.mission;

import org.hibernate.annotations.DynamicUpdate;

import com.biengual.core.domain.entity.BaseEntity;
import com.biengual.core.enums.MissionStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "mission")
@Getter
@DynamicUpdate
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MissionEntity extends BaseEntity {
    @Id // userId 를 PK로 사용
    @Column(name = "id")
    private Long userId;

    @Column(name = "one_content", nullable = false)
    private boolean oneContent;

    @Column(nullable = false)
    private boolean bookmark;

    @Column(nullable = false)
    private boolean quiz;

    @Enumerated(EnumType.STRING)
    @Column(name = "mission_status", nullable = false)
    private MissionStatus missionStatus = MissionStatus.IN_PROGRESS;

    @Builder
    public MissionEntity(Long userId) {
        this.userId = userId;
    }

    public static MissionEntity createByUserId(Long userId){
        return MissionEntity.builder()
            .userId(userId)
            .build();
    }

    @PrePersist
    protected void onCreate() {
        oneContent = false;
        bookmark = false;
        quiz = false;
    }
}
