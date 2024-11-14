package com.biengual.userapi.dashboard.infrastructure;

import com.biengual.core.annotation.DataProvider;
import com.biengual.core.util.PeriodUtil;
import com.biengual.userapi.dashboard.domain.DashboardInfo;
import com.biengual.userapi.dashboard.domain.DashboardReader;
import com.biengual.userapi.learning.domain.CategoryLearningHistoryCustomRepository;
import com.biengual.userapi.learning.domain.RecentLearningHistoryCustomRepository;
import com.biengual.userapi.user.domain.UserCustomRepository;
import lombok.RequiredArgsConstructor;

import java.time.YearMonth;
import java.util.List;

@DataProvider
@RequiredArgsConstructor
public class DashboardReaderImpl implements DashboardReader {
    private final RecentLearningHistoryCustomRepository recentLearningHistoryCustomRepository;
    private final CategoryLearningHistoryCustomRepository categoryLearningHistoryCustomRepository;
    private final UserCustomRepository userCustomRepository;

    @Override
    public DashboardInfo.RecentLearningSummary findRecentLearningSummary(Long userId) {
        return recentLearningHistoryCustomRepository.findRecentLearningSummaryByUserId(userId);
    }

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

    // 현재 유저 포인트 조회
    @Override
    public Long findCurrentPoint(Long userId) {
        return userCustomRepository.findUserPointByUserId(userId);
    }
}
