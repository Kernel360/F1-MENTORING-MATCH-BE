package com.biengual.userapi.dashboard.infrastructure;

import com.biengual.core.annotation.DataProvider;
import com.biengual.core.util.PeriodUtil;
import com.biengual.userapi.dashboard.domain.DashboardInfo;
import com.biengual.userapi.dashboard.domain.DashboardReader;
import com.biengual.userapi.learning.domain.CategoryLearningHistoryCustomRepository;
import com.biengual.userapi.learning.domain.RecentLearningHistoryCustomRepository;
import lombok.RequiredArgsConstructor;

import java.time.YearMonth;
import java.util.List;

@DataProvider
@RequiredArgsConstructor
public class DashboardReaderImpl implements DashboardReader {
    private final RecentLearningHistoryCustomRepository recentLearningHistoryCustomRepository;
    private final CategoryLearningHistoryCustomRepository categoryLearningHistoryCustomRepository;

    // 최근 학습 컨텐츠 조회
    @Override
    public List<DashboardInfo.RecentLearning> findRecentLearning(Long userId) {
        return recentLearningHistoryCustomRepository.findRecentLearningTop8ByUserId(userId);
    }

    // 기간에 따른 카테고리별 학습량 조회
    @Override
    public DashboardInfo.CategoryLearningList findCategoryLearning(Long userId, String date) {
        YearMonth yearMonth = PeriodUtil.toYearMonth(date);

        Long totalCount = categoryLearningHistoryCustomRepository.countCategoryLearningByUserIdInMonth(userId, yearMonth);

        List<DashboardInfo.CategoryLearning> categoryLearningList =
            categoryLearningHistoryCustomRepository.findCategoryLearningByUserIdInMonth(userId, yearMonth);

        return DashboardInfo.CategoryLearningList.of(totalCount, categoryLearningList);
    }
}
