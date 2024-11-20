package com.biengual.userapi.dashboard.presentation.dto;

import com.biengual.core.enums.ContentLevel;
import com.biengual.core.enums.ContentType;
import lombok.Builder;

import java.math.BigDecimal;
import java.util.List;

public class GetRecentLearningDto {

    public record RecentLearning(
        Long contentId,
        String title,
        String thumbnailUrl,
        ContentType contentType,
        String preScripts,
        String category,
        String duration,
        Integer hits,
        ContentLevel calculatedLevel,
        Boolean isScrapped,
        BigDecimal currentLearningRate,
        BigDecimal completedLearningRate
    ) {
    }

    @Builder
    public record Response(
        List<RecentLearning> recentLearningPreview
    ) {
    }
}
