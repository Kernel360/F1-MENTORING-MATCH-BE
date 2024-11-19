package com.biengual.userapi.recommender.domain;

import java.util.List;

import com.biengual.core.enums.ContentType;

import lombok.Builder;

public class RecommenderInfo {

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
    public record PreviewRecommender(
        List<Preview> recommendedContents
    ) {
        public static PreviewRecommender of(List<Preview> recommendedContents) {
            return PreviewRecommender.builder()
                .recommendedContents(recommendedContents)
                .build();
        }
    }
}
