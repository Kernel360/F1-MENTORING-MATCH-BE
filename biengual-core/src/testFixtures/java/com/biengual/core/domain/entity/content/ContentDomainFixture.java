package com.biengual.core.domain.entity.content;


import com.biengual.core.domain.entity.category.CategoryEntity;
import com.biengual.core.enums.ContentStatus;
import com.biengual.core.enums.ContentType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

public class ContentDomainFixture {

    private ContentDomainFixture() {
        throw new IllegalStateException("Utility class");
    }

    private static final ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());

    @Getter
    @Builder
    public static class TestContentEntity {
        private @Builder.Default Long id = 13414L;
        private @Builder.Default String url = "url/test";
        private @Builder.Default String title = "test title";
        private @Builder.Default int hits = 0;
        private @Builder.Default String thumbnailUrl = "thumbnail-url/test";
        private @Builder.Default String preScripts = "test...";
        private @Builder.Default ContentType contentType = ContentType.READING;
        private @Builder.Default ContentStatus contentStatus = ContentStatus.ACTIVATED;
        private @Builder.Default String mongoContentId = "mongoObjectId";
        private @Builder.Default CategoryEntity category = null;
        private @Builder.Default Integer numOfQuiz = 0;
        private @Builder.Default LocalDateTime createdAt = LocalDateTime.now();
        private @Builder.Default LocalDateTime updatedAt = LocalDateTime.now();

        public static TestContentEntity.TestContentEntityBuilder createContentEntity() {
            return TestContentEntity.builder();
        }

        public ContentEntity get() {
            return mapper.convertValue(this, ContentEntity.class);
        }
    }

    @Getter
    @Builder
    public static class TestContentEntityWithVersion {
        private @Builder.Default Long id = 13414L;
        private @Builder.Default String url = "url/test";
        private @Builder.Default String title = "test title";
        private @Builder.Default int hits = 0;
        private @Builder.Default String thumbnailUrl = "thumbnail-url/test";
        private @Builder.Default String preScripts = "test...";
        private @Builder.Default ContentType contentType = ContentType.READING;
        private @Builder.Default ContentStatus contentStatus = ContentStatus.ACTIVATED;
        private @Builder.Default String mongoContentId = "mongoObjectId";
        private @Builder.Default CategoryEntity category = null;
        private @Builder.Default Integer numOfQuiz = 0;
        private @Builder.Default LocalDateTime createdAt = LocalDateTime.now();
        private @Builder.Default LocalDateTime updatedAt = LocalDateTime.now();
        private @Builder.Default Integer version = 1;

        public static TestContentEntityWithVersion.TestContentEntityWithVersionBuilder createContentEntityWithVersion() {
            return TestContentEntityWithVersion.builder();
        }

        public ContentEntityWithVersion get() {
            return mapper.convertValue(this, ContentEntityWithVersion.class);
        }
    }
}