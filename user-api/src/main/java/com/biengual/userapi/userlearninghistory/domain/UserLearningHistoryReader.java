package com.biengual.userapi.userlearninghistory.domain;

import com.biengual.core.domain.entity.userlearninghistory.UserLearningHistoryEntity;

import java.util.List;

/**
 * UserLearningHistory 도메인의 DataProvider 계층의 인터페이스
 *
 * @author 문찬욱
 */
public interface UserLearningHistoryReader {
    List<UserLearningHistoryEntity> findRecentHistory();
}
