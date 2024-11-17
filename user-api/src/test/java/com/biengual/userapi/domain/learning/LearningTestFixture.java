package com.biengual.userapi.domain.learning;

import com.biengual.userapi.learning.domain.LearningCommand;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Test에 사용할 Learning 도메인의 객체 생성을 위한 TestFixture 클래스
 *
 * @author 문찬욱
 */
public class LearningTestFixture {

    private LearningTestFixture() {
    }

    private static final ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());

    @Getter
    @Builder
    public static class TestCommandRecordLearningRate {
        private @Builder.Default Long userId = 12L;
        private @Builder.Default Long contentId = 45L;
        private @Builder.Default BigDecimal learningRate = BigDecimal.valueOf(41.1);
        private @Builder.Default LocalDateTime learningTime = LocalDateTime.now();

        public static TestCommandRecordLearningRate.TestCommandRecordLearningRateBuilder createCommandRecordLearningRate() {
            return TestCommandRecordLearningRate.builder();
        }

        public LearningCommand.RecordLearningRate get() {
            return mapper.convertValue(this, LearningCommand.RecordLearningRate.class);
        }
    }
}
