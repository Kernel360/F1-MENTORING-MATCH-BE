package com.biengual.userapi.learning.presentation.dto;

import java.math.BigDecimal;

public class UpdateLearningRateDto {
    public record Request(
       Long contentId,
       BigDecimal learningRate
    ) {
    }
}
