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
    public DashboardInfo.RecentLearningList getRecentLearning(Long userId) {
        return DashboardInfo.RecentLearningList.of(dashboardReader.findRecentLearning(userId));
    }

    // 기간에 따른 카테고리별 학습량 조회
    @Override
    public DashboardInfo.CategoryLearningList getCategoryLearning(Long userId, String date) {
        return dashboardReader.findCategoryLearning(userId, date);
    }

    // 현재 유저 포인트 조회
    @Override
    public Long getCurrentPoint(Long userId) {
        return dashboardReader.findCurrentPoint(userId);
    }
}
