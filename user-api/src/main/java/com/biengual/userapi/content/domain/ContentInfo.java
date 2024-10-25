package com.biengual.userapi.content.domain;

import com.biengual.userapi.content.domain.enums.ContentType;

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

    public record PreviewContents(
        List<PreviewContent> previewContents
    ) {
    }
}
