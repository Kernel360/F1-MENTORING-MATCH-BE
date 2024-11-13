package com.biengual.userapi.concurrency;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
@DisplayName("최근 학습 도메인 동시성 테스트")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class RecentLearningConcurrencyTest {

    @Test
    @DisplayName("처음 학습한 컨텐츠에 대한 최근 학습 내역 저장 동시성 테스트")
    void recordRecentLearningHistoryTest_whenFirstLearning() {
    }
}
