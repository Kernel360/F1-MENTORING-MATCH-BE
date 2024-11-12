package com.biengual.userapi.learning.domain;

import com.biengual.core.domain.entity.learning.CategoryLearningHistoryEntity;
import com.biengual.core.domain.entity.learning.LearningHistoryEntity;
import com.biengual.core.domain.entity.learning.UserLearningHistoryEntity;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class LearningCommand {

    @Builder
    public record RecordLearningRate(
        Long userId,
        Long contentId,
        BigDecimal learningRate,
        LocalDateTime learningTime
    ) {
        public UserLearningHistoryEntity toUserLearningHistoryEntity() {
            return UserLearningHistoryEntity.builder()
                .userId(this.userId)
                .contentId(this.contentId)
                .learningRate(this.learningRate)
                .recentLearningTime(this.learningTime)
                .build();
        }

        public LearningHistoryEntity toLearningHistoryEntity() {
            return LearningHistoryEntity.builder()
                .userId(this.userId)
                .contentId(this.contentId)
                .learningRate(this.learningRate)
                .learningTime(this.learningTime)
                .build();
        }

        public CategoryLearningHistoryEntity toCategoryLearningHistoryEntity(Long categoryId) {
            return CategoryLearningHistoryEntity.builder()
                .userId(this.userId)
                .categoryId(categoryId)
                .learningTime(this.learningTime)
                .build();
        }
    }
}
