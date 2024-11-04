package com.biengual.userapi.userlearninghistory.domain;

import java.math.BigDecimal;

/**
 * UserLearningHistory 도메인의 DataProvider 계층의 인터페이스
 *
 * @author 문찬욱
 */
public interface UserLearningHistoryStore {
    void recordContentLearning(Long userId, Long contentId, BigDecimal learningRate);
}
