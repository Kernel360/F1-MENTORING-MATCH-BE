package com.biengual.userapi.mission.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import com.biengual.core.domain.entity.mission.MissionEntity;

public interface MissionRepository extends JpaRepository<MissionEntity, Long> {
}
