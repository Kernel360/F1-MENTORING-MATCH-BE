package com.biengual.userapi.dashboard.domain;

import com.biengual.core.enums.ContentType;

import java.util.List;

public class DashboardInfo {

    public record RecentLearning(
        Long contentId,
        String title,
        String thumbnailUrl,
        ContentType contentType,
        String preScripts,
        String category,
        Integer hits,
        Boolean isScrapped,
        Integer learningRate
    ) {
    }

    public record RecentLearnings(
        List<RecentLearning> recentLearningPreview
    ) {
    }
}
