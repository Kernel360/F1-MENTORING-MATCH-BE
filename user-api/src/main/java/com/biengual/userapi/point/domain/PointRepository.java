package com.biengual.userapi.point.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import com.biengual.core.domain.entity.point.PointEntity;

public interface PointRepository extends JpaRepository<PointEntity, Long> {
}
