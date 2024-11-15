package com.biengual.userapi.dashboard.infrastructure;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.biengual.core.annotation.DataProvider;
import com.biengual.core.domain.entity.pointhistory.PointHistoryEntity;
import com.biengual.core.util.PeriodUtil;
import com.biengual.userapi.dashboard.domain.DashboardInfo;
import com.biengual.userapi.dashboard.domain.DashboardReader;
import com.biengual.userapi.learning.domain.CategoryLearningHistoryCustomRepository;
import com.biengual.userapi.learning.domain.RecentLearningHistoryCustomRepository;
import com.biengual.userapi.missionhistory.domain.MissionHistoryCustomRepository;
import com.biengual.userapi.pointhistory.domain.PointHistoryCustomRepository;
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
    private final PointHistoryCustomRepository pointHistoryCustomRepository;

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

        Long totalCount = categoryLearningHistoryCustomRepository.countCategoryLearningByUserIdInMonth(userId,
            yearMonth);

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
    public DashboardInfo.QuestionWeeklySummary findQuestionSummary(Long userId, String date) {
        LocalDate currentDate = PeriodUtil.toLocalDate(date);
        return DashboardInfo.QuestionWeeklySummary.of(
            questionHistoryCustomRepository.findQuestionHistoryByUserIdLastFiveWeeks(userId, currentDate)
        );
    }

    // 포인트 내역 조회
    @Override
    public DashboardInfo.MonthlyPointHistory findMonthlyPointHistory(Long userId, String date) {
        YearMonth yearMonth = PeriodUtil.toYearMonth(date);

        List<PointHistoryEntity> monthlyPointHistoryList =
            pointHistoryCustomRepository.findPointHistoryByUserIdInMonth(userId, yearMonth);

        Map<LocalDate, List<DashboardInfo.PointRecord>> dailyPointHistoryMap =
            groupByDailyPointHistoryMap(monthlyPointHistoryList);

        List<DashboardInfo.DailyPointHistory> dailyPointHistoryList =
            dailyPointHistoryMapToListInfo(dailyPointHistoryMap);

        Long currentPoint = userCustomRepository.findUserPointByUserId(userId);

        return DashboardInfo.MonthlyPointHistory.of(currentPoint, dailyPointHistoryList);
    }

    // Internal Method =================================================================================================

    // Map 형식의 DailyPointHistory로 grouping 하는 메서드
    private Map<LocalDate, List<DashboardInfo.PointRecord>> groupByDailyPointHistoryMap(
        List<PointHistoryEntity> monthlyPointHistoryList
    ) {
        return monthlyPointHistoryList.stream()
            .collect(
                Collectors.groupingBy(
                    history -> history.getCreatedAt().toLocalDate(),
                    Collectors.mapping(
                        history -> DashboardInfo.PointRecord.of(history.getReason(), history.getPointChange()),
                        Collectors.toList()
                    )
                )
            );
    }

    // Map 형식의 DailyPointHistory을 List<DashboardInfo.DailyPointHistory>로 변환
    private List<DashboardInfo.DailyPointHistory> dailyPointHistoryMapToListInfo(
        Map<LocalDate, List<DashboardInfo.PointRecord>> dailyPointHistoryMap
    ) {
        return dailyPointHistoryMap.entrySet().stream()
            .map(entry -> new DashboardInfo.DailyPointHistory(
                entry.getKey(),
                entry.getValue()
            ))
            .sorted(Comparator.comparing(DashboardInfo.DailyPointHistory::date).reversed())
            .toList();
    }
}
