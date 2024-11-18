package com.biengual.userapi.recommender.presentation;

import java.util.List;

import com.biengual.core.enums.ContentType;

import lombok.Builder;

public class RecommenderDto {

    public record Preview(
        Long contentId,
        String title,
        String thumbnailUrl,   // coverImageUrl
        ContentType contentType,
        String preScripts,     // description
        String category,
        Integer hits,
        Integer videoDurationInSeconds,
        Boolean isScrapped,
        Boolean isPointRequired
    ) {
    }

    @Builder
    public record Response(
        List<Preview> recommendedContents
    ) {
    }
}
