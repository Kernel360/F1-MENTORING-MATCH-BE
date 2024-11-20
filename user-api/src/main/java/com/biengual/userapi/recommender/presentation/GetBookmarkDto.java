package com.biengual.userapi.recommender.presentation;

import java.util.List;

import lombok.Builder;

public class GetBookmarkDto {

    @Builder
    public record Popular(
        String enDetail,
        String koDetail,
        Long contentId
    ) {
    }

    @Builder
    public record Response(
        List<Popular> popularBookmarks
    ) {
    }
}
