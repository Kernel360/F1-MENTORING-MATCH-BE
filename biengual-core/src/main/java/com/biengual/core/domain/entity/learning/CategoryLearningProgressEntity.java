package com.biengual.core.domain.entity.learning;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

import static com.biengual.core.constant.RestrictionConstant.LEARNING_COMPLETION_RATE_THRESHOLD;

@Getter
@Entity
@Table(name = "category_learning_progress")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CategoryLearningProgressEntity {
    @EmbeddedId
    private CategoryLearningProgressId categoryLearningProgressId;

    @Column(nullable = false, columnDefinition = "bigint")
    private Long totalLearningCount;

    @Column(nullable = false, columnDefinition = "bigint")
    private Long completedLearningCount;

    @Builder
    public CategoryLearningProgressEntity(
        CategoryLearningProgressId categoryLearningProgressId, Long totalLearningCount, Long completedLearningCount
    ) {
        this.categoryLearningProgressId = categoryLearningProgressId;
        this.totalLearningCount = totalLearningCount;
        this.completedLearningCount = completedLearningCount;
    }

    // 학습 상황 업데이트
    public void updateProgress(BigDecimal learningRate) {
        this.totalLearningCount += 1;
        this.completedLearningCount =
            learningRate.compareTo(BigDecimal.valueOf(LEARNING_COMPLETION_RATE_THRESHOLD)) >= 0
            ? this.completedLearningCount + 1
            : this.completedLearningCount;
    }
}
