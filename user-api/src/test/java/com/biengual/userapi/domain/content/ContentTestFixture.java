package com.biengual.userapi.domain.content;

import com.biengual.core.domain.entity.category.CategoryEntity;
import com.biengual.core.domain.entity.content.ContentEntity;
import com.biengual.core.enums.ContentLevel;
import com.biengual.core.enums.ContentStatus;
import com.biengual.core.enums.ContentType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

/**
 * Test에 사용할 Content 도메인의 객체 생성을 위한 TestFixture 클래스
 *
 * @author 문찬욱
 */
public class ContentTestFixture {

    private ContentTestFixture() {
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
        private @Builder.Default Integer videoDuration = null;
        private @Builder.Default ContentLevel contentLevel = null;
        private @Builder.Default LocalDateTime createdAt = LocalDateTime.now();
        private @Builder.Default LocalDateTime updatedAt = LocalDateTime.now();

        public static TestContentEntity.TestContentEntityBuilder createContentEntity() {
            return TestContentEntity.builder();
        }

        public ContentEntity get() {
            return mapper.convertValue(this, ContentEntity.class);
        }
    }
}
