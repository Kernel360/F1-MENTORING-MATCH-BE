package com.biengual.userapi.learning.domain;

import com.biengual.core.domain.entity.userlearninghistory.UserLearningHistoryEntity;
import lombok.Builder;

public class LearningCommand {

    @Builder
    public record UpdateLearningRate(
        Long userId,
        Long contentId,
        Integer learningRate
    ) {
        public UserLearningHistoryEntity toUserLearningHistoryEntity() {
            return UserLearningHistoryEntity.builder()
                .userId(this.userId)
                .contentId(this.contentId)
                .learningRate(this.learningRate)
                .build();
        }
    }
}
