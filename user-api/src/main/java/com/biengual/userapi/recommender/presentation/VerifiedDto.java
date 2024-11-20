package com.biengual.userapi.recommender.presentation;

import lombok.Builder;

public class VerifiedDto {

    @Builder
    public record Bookmark(
        Long contentId,
        Long sentenceIndex,
        String enDetail
    ) {
    }
}