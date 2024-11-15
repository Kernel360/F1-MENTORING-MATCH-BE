package com.biengual.userapi.dashboard.application;

import com.biengual.userapi.dashboard.domain.DashboardInfo;
import com.biengual.userapi.dashboard.domain.DashboardReader;
import com.biengual.userapi.dashboard.domain.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService {
    private final DashboardReader dashboardReader;

    // 최근 학습 컨텐츠 1개 요약하여 조회
    @Override
    @Transactional(readOnly = true)
    public DashboardInfo.RecentLearningSummary getRecentLearningSummary(Long userId) {
        return dashboardReader.findRecentLearningSummary(userId);
    }

    // 최근 학습 컨텐츠 조회
    @Override
    @Transactional(readOnly = true)
    public DashboardInfo.RecentLearningList getRecentLearning(Long userId) {
        return DashboardInfo.RecentLearningList.of(dashboardReader.findRecentLearning(userId));
    }

    // 기간에 따른 카테고리별 학습량 조회
    @Override
    @Transactional(readOnly = true)
    public DashboardInfo.CategoryLearningList getCategoryLearning(Long userId, String date) {
        return dashboardReader.findCategoryLearning(userId, date);
    }

    // 현재 유저 포인트 조회
    @Override
    public Long getCurrentPoint(Long userId) {
        return dashboardReader.findCurrentPoint(userId);
    }

    // 미션 달력 조회
    @Override
    public DashboardInfo.MissionCalendar getMissionCalendar(Long userId, String date) {
        return DashboardInfo.MissionCalendar.of(dashboardReader.findMissionHistory(userId, date));
    }

    // 포인트 내역 조회
    @Override
    public DashboardInfo.MonthlyPointHistory getMonthlyPointHistory(Long userId, String date) {
        return dashboardReader.findMonthlyPointHistory(userId, date);
    }
}
