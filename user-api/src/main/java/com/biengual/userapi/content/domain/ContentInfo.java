package com.biengual.userapi.content.domain;

import lombok.Builder;

import java.util.List;

public class ContentInfo {

    public record PreviewContent(
        Long contentId,
        String title,
        String thumbnailUrl,
        ContentType contentType,
        String preScripts,
        String category,
        Integer hits
    ) {
    }

    @Builder
    public record PreviewContents(
        List<PreviewContent> previewContents
    ) {
        public static PreviewContents of(List<PreviewContent> previewContents) {
            return PreviewContents.builder()
                .previewContents(previewContents)
                .build();
        }
    }

    public record ViewContent(
        Long contentId,
        String title,
        String thumbnailUrl,
        ContentType contentType,
        String preScripts,
        String category,
        Integer hits
    ) {
    }
}
