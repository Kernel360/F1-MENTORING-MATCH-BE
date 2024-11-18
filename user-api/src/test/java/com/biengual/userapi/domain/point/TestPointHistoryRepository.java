package com.biengual.userapi.domain.point;

import com.biengual.core.domain.entity.pointhistory.PointHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TestPointHistoryRepository extends JpaRepository<PointHistoryEntity, Long> {
    Optional<PointHistoryEntity> findTopByOrderByIdDesc();
}
