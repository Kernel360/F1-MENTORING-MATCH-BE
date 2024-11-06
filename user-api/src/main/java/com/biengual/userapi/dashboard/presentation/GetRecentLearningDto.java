package com.biengual.userapi.dashboard.presentation;

import com.biengual.core.enums.ContentType;
import lombok.Builder;

import java.util.List;

public class GetRecentLearningDto {

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

    @Builder
    public record Response(
        List<RecentLearning> recentLearningPreview
    ) {
    }
}
