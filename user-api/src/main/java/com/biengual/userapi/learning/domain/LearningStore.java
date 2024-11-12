package com.biengual.userapi.learning.domain;

/**
 * UserLearningHistory 도메인의 DataProvider 계층의 인터페이스
 *
 * @author 문찬욱
 */
public interface LearningStore {
    void recordRecentLearningHistory(LearningCommand.UpdateLearningRate command);
}
