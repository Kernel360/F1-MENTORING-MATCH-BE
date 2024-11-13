package com.biengual.userapi.dashboard.presentation.dto;

import lombok.Builder;

import java.math.BigDecimal;
import java.util.List;

public class GetCategoryLearningDto {

    public record CategoryLearning(
        Long categoryId,
        String categoryName,
        Integer count,
        BigDecimal ratio
    ) {
    }

    @Builder
    public record Response(
        List<CategoryLearning> categoryLearningList
    ) {
    }
}
