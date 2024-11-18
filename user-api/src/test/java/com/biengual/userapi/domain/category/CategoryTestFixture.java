package com.biengual.userapi.domain.category;

import com.biengual.core.domain.entity.category.CategoryEntity;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.Builder;
import lombok.Getter;

/**
 * Test에 사용할 Category 도메인의 객체 생성을 위한 TestFixture 클래스
 *
 * @author 문찬욱
 */
public class CategoryTestFixture {

    private CategoryTestFixture() {
    }

    private static final ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());

    @Getter
    @Builder
    public static class TestCategoryEntity {
        private @Builder.Default Long id = 3L;
        private @Builder.Default String name = "Concurrency";

        public static TestCategoryEntity.TestCategoryEntityBuilder createCategoryEntity() {
            return TestCategoryEntity.builder();
        }

        public CategoryEntity get() {
            return mapper.convertValue(this, CategoryEntity.class);
        }
    }
}
