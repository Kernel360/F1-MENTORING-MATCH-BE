package com.biengual.userapi.learning.domain;

import com.biengual.core.domain.entity.learning.CategoryLearningHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryLearningHistoryRepository extends JpaRepository<CategoryLearningHistoryEntity, Long> {
}
