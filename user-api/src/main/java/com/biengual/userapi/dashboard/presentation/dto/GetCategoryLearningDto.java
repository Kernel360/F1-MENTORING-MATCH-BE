package com.biengual.userapi.dashboard.presentation.dto;

import lombok.Builder;

import java.util.List;

public class GetCategoryLearningDto {

    public record CategoryLearning(
        Long categoryId,
        String categoryName,
        Long count
    ) {
    }

    @Builder
    public record Response(
        Long totalCount,
        List<CategoryLearning> categoryLearningList
    ) {
    }
}
