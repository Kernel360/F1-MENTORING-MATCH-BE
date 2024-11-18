package com.biengual.userapi.learning.presentation.dto;

import java.math.BigDecimal;

public class RecordLearningRateDto {
    public record Request(
       Long contentId,
       BigDecimal learningRate
    ) {
    }
}
