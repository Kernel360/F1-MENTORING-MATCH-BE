package com.biengual.userapi.dashboard.domain;

import java.util.List;

/**
 * Dashboard 도메인의 DataProvider 계층의 인터페이스
 *
 * @author 문찬욱
 */
public interface DashboardReader {
    DashboardInfo.RecentLearningSummary findRecentLearningSummary(Long userId);

    List<DashboardInfo.RecentLearning> findRecentLearning(Long userId);

    DashboardInfo.CategoryLearningList findCategoryLearning(Long userId, String date);

    Long findCurrentPoint(Long userId);

    List<DashboardInfo.MissionHistory> findMissionHistory(Long userId, String date);

    DashboardInfo.QuestionWeeklySummary findQuestionSummary(Long userId, String date);

    DashboardInfo.MonthlyPointHistory findMonthlyPointHistory(Long userId, String date);
}
