package com.biengual.userapi.learning.domain;

/**
 * Learning 도메인의 Service 계층의 인터페이스
 */
public interface LearningService {
    void recordLearningRate(LearningCommand.RecordLearningRate command);


}
