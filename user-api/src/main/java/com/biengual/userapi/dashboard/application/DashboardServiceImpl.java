package com.biengual.userapi.dashboard.application;

import com.biengual.userapi.dashboard.domain.DashboardInfo;
import com.biengual.userapi.dashboard.domain.DashboardReader;
import com.biengual.userapi.dashboard.domain.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService {
    private final DashboardReader dashboardReader;

    // 최근 학습 컨텐츠 조회
    @Override
    public DashboardInfo.RecentLearnings getRecentLearning(Long userId) {
        return DashboardInfo.RecentLearnings.of(dashboardReader.findRecentLearning(userId));
    }
}
