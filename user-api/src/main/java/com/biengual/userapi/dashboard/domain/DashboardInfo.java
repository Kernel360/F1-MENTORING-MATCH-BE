package com.biengual.userapi.dashboard.domain;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import com.biengual.core.enums.ContentLevel;
import com.biengual.core.enums.ContentType;
import com.biengual.core.enums.PointReason;

import lombok.Builder;

public class DashboardInfo {

    public record RecentLearning(
        Long contentId,
        String title,
        String thumbnailUrl,
        ContentType contentType,
        String preScripts,
        String category,
        Integer videoDurationInSeconds,
        Integer hits,
        ContentLevel calculatedLevel,
        Boolean isScrapped,
        BigDecimal currentLearningRate,
        BigDecimal completedLearningRate
    ) {
    }

    @Builder
    public record RecentLearningList(
        List<RecentLearning> recentLearningPreview
    ) {
        public static RecentLearningList of(List<RecentLearning> recentLearningList) {
            return RecentLearningList.builder()
                .recentLearningPreview(recentLearningList)
                .build();
        }
    }

    public record CategoryLearning(
        Long categoryId,
        String categoryName,
        Long count
    ) {
    }

    @Builder
    public record CategoryLearningList(
        Long totalCount,
        List<CategoryLearning> categoryLearningList
    ) {
        public static CategoryLearningList of(Long totalCount, List<CategoryLearning> categoryLearningList) {
            return CategoryLearningList.builder()
                .totalCount(totalCount)
                .categoryLearningList(categoryLearningList)
                .build();
        }
    }

    public record RecentLearningSummary(
        String title,
        BigDecimal completedLearningRate
    ) {
    }

    public record MissionStatus(
        Boolean oneContent,
        Boolean bookmark,
        Boolean quiz,
        Integer count
    ) {
    }

    public record MissionHistory(
        LocalDateTime date,
        MissionStatus missionStatus
    ) {
    }

    @Builder
    public record MissionCalendar(
        List<MissionHistory> missionHistoryList
    ) {
        public static MissionCalendar of(List<MissionHistory> missionHistoryList) {
            return MissionCalendar.builder()
                .missionHistoryList(missionHistoryList)
                .build();
        }
    }

    @Builder
    public record QuestionSummary(
        LocalDate weekStartDate,
        int weekNumber,
        Integer firstTryCorrect,
        Integer reTryCorrect,
        Integer totalFirstTry,
        Integer totalReTry
    ) {
        public static QuestionSummary of(
            LocalDate weekStartDate,
            int weekNumber,
            Integer firstTryCorrect,
            Integer reTryCorrect,
            Integer totalFirstTry,
            Integer totalReTry
        ) {
            return QuestionSummary.builder()
                .weekStartDate(weekStartDate)
                .weekNumber(weekNumber)
                .firstTryCorrect(firstTryCorrect)
                .reTryCorrect(reTryCorrect)
                .totalFirstTry(totalFirstTry)
                .totalReTry(totalReTry)
                .build();
        }
    }

    @Builder
    public record QuestionWeeklySummary(
        List<QuestionSummary> questionSummaryList
    ) {
        public static QuestionWeeklySummary of(List<QuestionSummary> questionSummaryList) {
            return QuestionWeeklySummary.builder()
                .questionSummaryList(questionSummaryList)
                .build();
        }
    }

    @Builder
    public record PointRecord(
        PointReason reason,
        Long point
    ) {
        public static PointRecord of(PointReason reason, Long point) {
            return PointRecord.builder()
                .reason(reason)
                .point(point)
                .build();
        }
    }

    public record DailyPointHistory(
        LocalDate date,
        List<PointRecord> pointsHistory
    ) {
    }

    @Builder
    public record MonthlyPointHistory(
        Long currentPoint,
        List<DailyPointHistory> dailyPointHistoryList
    ) {
        public static MonthlyPointHistory of(Long currentPoint, List<DailyPointHistory> dailyPointHistoryList) {
            return MonthlyPointHistory.builder()
                .currentPoint(currentPoint)
                .dailyPointHistoryList(dailyPointHistoryList)
                .build();
        }
    }
}
