package com.biengual.userapi.recommender.domain;

import java.util.List;

import com.biengual.core.domain.entity.recommender.BookmarkRecommenderEntity;
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

    @Builder
    public record PopularBookmark(
        String enDetail,
        String koDetail,
        Long contentId
    ) {
        public static PopularBookmark of(BookmarkRecommenderEntity bookmarkRecommender) {
            return PopularBookmark.builder()
                .enDetail(bookmarkRecommender.getEnDetail())
                .koDetail(bookmarkRecommender.getKoDetail())
                .contentId(bookmarkRecommender.getContentId())
                .build();
        }
    }

    @Builder
    public record PopularBookmarkRecommender(
        List<PopularBookmark> popularBookmarks
    ) {
        public static PopularBookmarkRecommender of(List<PopularBookmark> popularBookmarkBookmarks) {
            return PopularBookmarkRecommender.builder()
                .popularBookmarks(popularBookmarkBookmarks)
                .build();
        }
    }
}
