package com.biengual.core.domain.entity.learning;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "category_learning_progress")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CategoryLearningProgress {
    @EmbeddedId
    private CategoryLearningProgressId categoryLearningProgressId;

    @Column(nullable = false, columnDefinition = "bigint")
    private Long totalLearningCount;

    @Column(nullable = false, columnDefinition = "bigint")
    private Long completedLearningCount;
}
