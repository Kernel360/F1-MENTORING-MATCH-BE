package com.biengual.userapi.category.domain;

import java.util.List;

public class CategoryInfo {

    public record Category(
        Long id,
        String name
    ) {
    }

    public record AllCategories(
        List<Category> categories
    ) {
    }
}
