package com.biengual.userapi.content.domain;

import com.biengual.core.domain.document.content.script.Script;
import com.biengual.core.domain.entity.content.ContentLevelFeedbackDataMart;
import com.biengual.core.enums.ContentLevel;
import com.biengual.core.enums.ContentStatus;
import com.biengual.core.enums.ContentType;
import lombok.Builder;

import java.math.BigDecimal;
import java.util.List;

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
        ContentLevel contentLevel,
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
        ContentLevel contentLevel,
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
    public record LearningRateInfo(
        BigDecimal currentLearningRate,
        BigDecimal completedLearningRate
    ) {
        public static LearningRateInfo createInitLearningRateInfo() {
            return LearningRateInfo.builder()
                .currentLearningRate(BigDecimal.ZERO)
                .completedLearningRate(BigDecimal.ZERO)
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
        ContentLevel contentLevel,
        Boolean isScrapped,
        BigDecimal currentLearningRate,
        BigDecimal completedLearningRate,
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

    public record AggregatedLevelFeedback(
        Long contentId,
        Long levelLowCount,
        Long levelMediumCount,
        Long levelHighCount,
        Long feedbackTotalCount
    ) {
        public ContentLevelFeedbackDataMart toContentLevelFeedbackDataMart() {
            return ContentLevelFeedbackDataMart.createEntity(
                this.contentId,
                this.levelLowCount,
                this.levelMediumCount,
                this.levelHighCount,
                this.feedbackTotalCount
            );
        }
    }
}
