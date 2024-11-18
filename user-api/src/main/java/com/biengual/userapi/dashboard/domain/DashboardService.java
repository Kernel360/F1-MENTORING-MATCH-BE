package com.biengual.userapi.dashboard.domain;

/**
 * Dashboard 도메인의 Service 계층의 인터페이스
 */
public interface DashboardService {
    DashboardInfo.RecentLearningSummary getRecentLearningSummary(Long userId);

    DashboardInfo.RecentLearningList getRecentLearning(Long userId);

    DashboardInfo.CategoryLearningList getCategoryLearning(Long userId, String date);

    Long getCurrentPoint(Long userId);

    DashboardInfo.MissionCalendar getMissionCalendar(Long userId, String date);

    DashboardInfo.QuestionWeeklySummary getQuestionSummary(Long userId, String date);

    DashboardInfo.MonthlyPointHistory getMonthlyPointHistory(Long userId, String date);
}
