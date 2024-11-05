package com.biengual.userapi.learning.presentation.dto;

public class UpdateLearningRateDto {
    public record Request(
       Long contentId,
       Integer learningRate
    ) {
    }
}
