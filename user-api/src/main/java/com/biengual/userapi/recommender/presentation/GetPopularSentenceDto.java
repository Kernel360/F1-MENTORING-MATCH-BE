package com.biengual.userapi.recommender.presentation;

import java.util.List;

import com.biengual.core.enums.ContentType;
import lombok.Builder;

public class GetPopularSentenceDto {

    @Builder
    public record Bookmark(
        String enDetail,
        String koDetail,
        Long contentId,
        ContentType contentType
    ) {
    }

    @Builder
    public record Response(
        List<Bookmark> popularBookmarks
    ) {
    }
}
