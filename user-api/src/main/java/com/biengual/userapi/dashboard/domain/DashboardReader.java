package com.biengual.userapi.dashboard.domain;

import java.util.List;

/**
 * Dashboard 도메인의 DataProvider 계층의 인터페이스
 *
 * @author 문찬욱
 */
public interface DashboardReader {
    List<DashboardInfo.RecentLearning> findRecentLearning(Long userId);

    DashboardInfo.CategoryLearningList findCategoryLearning(Long userId, String date);

    Long findCurrentPoint(Long userId);
}
