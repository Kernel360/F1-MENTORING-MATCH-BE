package com.biengual.userapi.dashboard.presentation.dto;

import lombok.Builder;

import java.math.BigDecimal;

public class GetRecentLearningSummaryDto {

    @Builder
    public record Response(
        String title,
        BigDecimal learningRate
    ) {
    }
}
