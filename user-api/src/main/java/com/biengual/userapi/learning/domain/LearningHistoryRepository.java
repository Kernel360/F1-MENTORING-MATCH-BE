package com.biengual.userapi.learning.domain;

import com.biengual.core.domain.entity.learning.LearningHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LearningHistoryRepository extends JpaRepository<LearningHistoryEntity, Long> {
}
