package com.biengual.userapi.content.domain;

import java.math.BigDecimal;
import java.util.List;

import com.biengual.core.domain.document.content.script.Script;
import com.biengual.core.enums.ContentStatus;
import com.biengual.core.enums.ContentType;

import lombok.Builder;

public class ContentInfo {

    public record PreviewContent(
        Long contentId,
        String title,
        String thumbnailUrl,
        ContentType contentType,
        String preScripts,
        String category,
        Integer hits,
        Integer videoDurationInSeconds,
        Boolean isScrapped,
        Boolean isPointRequired
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
        Integer hits,
        Integer videoDurationInSeconds,
        Boolean isScrapped,
        Boolean isPointRequired
    ) {
    }

    @Builder
    public record UserScript(
        Script script,
        Long bookmarkId,
        Boolean isHighlighted,
        String description
    ) {
        public static List<UserScript> toResponse(List<Script> scripts) {
            return scripts.stream()
                .map(UserScript::of)
                .toList();
        }

        private static UserScript of(Script script) {
            return UserScript.builder()
                .script(script)
                .build();
        }
    }

    @Builder
    public record Detail(
        Long contentId,
        ContentType contentType,
        String category,
        String title,
        String thumbnailUrl,
        String videoUrl,
        Integer videoDurationInSeconds,
        Integer hits,
        Boolean isScrapped,
        BigDecimal learningRate,
        List<UserScript> scriptList
    ) {
    }

    @Builder
    public record Admin(
        Long contentId,
        String title,
        String category,
        ContentType contentType,
        Integer hits,
        Integer numOfQuiz,
        ContentStatus contentStatus
    ) {
    }

}
