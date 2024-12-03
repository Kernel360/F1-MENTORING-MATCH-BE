package com.biengual.userapi.recommender.presentation;

import lombok.Builder;

import java.util.List;

public class GetPopularSentenceDto {

    @Builder
    public record Bookmark(
        String enDetail,
        String koDetail,
        Long contentId
    ) {
    }

    @Builder
    public record Response(
        List<Bookmark> popularBookmarks
    ) {
    }
}
