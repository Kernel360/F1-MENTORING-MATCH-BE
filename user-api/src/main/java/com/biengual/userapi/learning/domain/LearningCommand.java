package com.biengual.userapi.learning.domain;

import com.biengual.core.domain.entity.learning.LearningHistoryEntity;
import com.biengual.core.domain.entity.learning.UserLearningHistoryEntity;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class LearningCommand {

    @Builder
    public record UpdateLearningRate(
        Long userId,
        Long contentId,
        BigDecimal learningRate
    ) {
        public UserLearningHistoryEntity toUserLearningHistoryEntity() {
            return UserLearningHistoryEntity.builder()
                .userId(this.userId)
                .contentId(this.contentId)
                .learningRate(this.learningRate)
                .recentLearningTime(LocalDateTime.now())
                .build();
        }
    }
}
