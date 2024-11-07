package com.biengual.userapi.missionhistory.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import com.biengual.core.domain.entity.missionhistory.MissionHistoryEntity;

public interface MissionHistoryRepository extends JpaRepository<MissionHistoryEntity, Long> {
}
