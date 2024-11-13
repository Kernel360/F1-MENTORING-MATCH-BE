package com.biengual.userapi.dashboard.domain;

import com.biengual.core.enums.ContentType;
import lombok.Builder;

import java.math.BigDecimal;
import java.util.List;

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
}
