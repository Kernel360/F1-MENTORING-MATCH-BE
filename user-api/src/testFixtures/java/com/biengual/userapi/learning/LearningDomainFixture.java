package com.biengual.userapi.learning;

import com.biengual.userapi.learning.domain.LearningCommand;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class LearningDomainFixture {

    private LearningDomainFixture() {
        throw new IllegalStateException("Utility class");
    }

    private static final ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());

    @Getter
    @Builder
    public static class TestCommandRecordLearningRate {
        private @Builder.Default Long userId = 121L;
        private @Builder.Default Long contentId = 43L;
        private @Builder.Default BigDecimal learningRate = BigDecimal.valueOf(24.1);
        private @Builder.Default LocalDateTime learningTime = LocalDateTime.now();

        public static TestCommandRecordLearningRate.TestCommandRecordLearningRateBuilder createCommandRecordLearningRate() {
            return TestCommandRecordLearningRate.builder();
        }

        public LearningCommand.RecordLearningRate get() {
            return mapper.convertValue(this, LearningCommand.RecordLearningRate.class);
        }
    }
}
