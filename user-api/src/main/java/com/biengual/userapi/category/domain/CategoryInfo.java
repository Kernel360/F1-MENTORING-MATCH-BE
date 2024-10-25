package com.biengual.userapi.category.domain;

import lombok.Builder;

import java.util.List;

public class CategoryInfo {

    public record Category(
        Long id,
        String name
    ) {
    }

    @Builder
    public record AllCategories(
        List<Category> categories
    ) {
        public static AllCategories of(List<Category> categories) {
            return AllCategories.builder()
                .categories(categories)
                .build();
        }
    }
}
