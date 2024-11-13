package com.biengual.userapi.dashboard.domain;

import com.biengual.core.enums.ContentType;
import lombok.Builder;

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
        Integer learningRate
    ) {
    }

    @Builder
    public record RecentLearnings(
        List<RecentLearning> recentLearningPreview
    ) {
        public static RecentLearnings of(List<RecentLearning> recentLearnings) {
            return RecentLearnings.builder()
                .recentLearningPreview(recentLearnings)
                .build();
        }
    }
}
