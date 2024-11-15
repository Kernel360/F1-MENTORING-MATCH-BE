package com.biengual.userapi.dashboard.infrastructure;

import java.time.YearMonth;
import java.util.List;

import com.biengual.core.annotation.DataProvider;
import com.biengual.core.util.PeriodUtil;
import com.biengual.userapi.dashboard.domain.DashboardInfo;
import com.biengual.userapi.dashboard.domain.DashboardReader;
import com.biengual.userapi.learning.domain.CategoryLearningHistoryCustomRepository;
import com.biengual.userapi.learning.domain.RecentLearningHistoryCustomRepository;
import com.biengual.userapi.missionhistory.domain.MissionHistoryCustomRepository;
import com.biengual.userapi.questionhistory.domain.QuestionHistoryCustomRepository;
import com.biengual.userapi.user.domain.UserCustomRepository;

import lombok.RequiredArgsConstructor;

@DataProvider
@RequiredArgsConstructor
public class DashboardReaderImpl implements DashboardReader {
    private final RecentLearningHistoryCustomRepository recentLearningHistoryCustomRepository;
    private final CategoryLearningHistoryCustomRepository categoryLearningHistoryCustomRepository;
    private final UserCustomRepository userCustomRepository;
    private final MissionHistoryCustomRepository missionHistoryCustomRepository;
    private final QuestionHistoryCustomRepository questionHistoryCustomRepository;

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

    // 미션 달력 조회
    @Override
    public List<DashboardInfo.MissionHistory> findMissionHistory(Long userId, String date) {
        YearMonth yearMonth = PeriodUtil.toYearMonth(date);

        return missionHistoryCustomRepository.findMissionHistoryByUserIdInMonth(userId, yearMonth);
    }

    @Override
    public DashboardInfo.QuestionSummary findQuestionSummary(Long userId, String date) {
        YearMonth yearMonth = PeriodUtil.toYearMonth(date);
        return questionHistoryCustomRepository.findQuestionHistoryByUserIdInMonth(userId, yearMonth);
    }
}