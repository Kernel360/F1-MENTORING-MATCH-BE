package com.biengual.userapi.recommender.presentation;

import java.util.List;

import lombok.Builder;

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
