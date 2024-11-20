package com.biengual.userapi.recommender.presentation;

import java.util.List;

import com.biengual.core.enums.ContentType;

import lombok.Builder;

public class GetPreviewDto {

    public record Preview(
        Long contentId,
        String title,
        String thumbnailUrl,   // coverImageUrl
        ContentType contentType,
        String category,
        Boolean isPointRequired
    ) {
    }

    @Builder
    public record Response(
        List<Preview> recommendedContents
    ) {
    }
}
