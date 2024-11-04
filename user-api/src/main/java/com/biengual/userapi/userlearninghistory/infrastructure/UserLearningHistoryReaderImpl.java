package com.biengual.userapi.userlearninghistory.infrastructure;

import com.biengual.core.annotation.DataProvider;
import com.biengual.core.domain.entity.userlearninghistory.UserLearningHistoryEntity;
import com.biengual.userapi.userlearninghistory.domain.UserLearningHistoryReader;
import com.biengual.userapi.userlearninghistory.domain.UserLearningHistoryRepository;
import lombok.RequiredArgsConstructor;

import java.util.List;

@DataProvider
@RequiredArgsConstructor
public class UserLearningHistoryReaderImpl implements UserLearningHistoryReader {
    private final UserLearningHistoryRepository userLearningHistoryRepository;

    // 최근 학습한 8개 내역 조회
    @Override
    public List<UserLearningHistoryEntity> findRecentHistory() {
        return userLearningHistoryRepository.findTop8ByOrderByRecentLearningTimeDesc();
    }
}
