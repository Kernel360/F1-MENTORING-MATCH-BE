package com.biengual.userapi.dashboard.presentation.dto;

public class GetQuestionSummaryDto {

    public record Response(
        Double firstTryCorrectRate,
        Double reTryCorrectRate
    ) {
    }
}
