package com.biengual.userapi.dashboard.infrastructure;

import com.biengual.core.annotation.DataProvider;
import com.biengual.userapi.dashboard.domain.DashboardInfo;
import com.biengual.userapi.dashboard.domain.DashboardReader;
import com.biengual.userapi.learning.domain.RecentLearningHistoryCustomRepository;
import lombok.RequiredArgsConstructor;

import java.util.List;

@DataProvider
@RequiredArgsConstructor
public class DashboardReaderImpl implements DashboardReader {
    private final RecentLearningHistoryCustomRepository recentLearningHistoryCustomRepository;

    // 최근 학습 컨텐츠 조회
    @Override
    public List<DashboardInfo.RecentLearning> findRecentLearning(Long userId) {
        return recentLearningHistoryCustomRepository.findRecentLearningTop8ByUserId(userId);
    }
}
