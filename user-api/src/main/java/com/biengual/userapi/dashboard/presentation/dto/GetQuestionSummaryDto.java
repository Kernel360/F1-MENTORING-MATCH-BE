package com.biengual.userapi.dashboard.presentation.dto;

import java.time.LocalDate;
import java.util.List;

import lombok.Builder;

public class GetQuestionSummaryDto {

    public record QuestionSummary(
        LocalDate weekStartDate,
        int weekNumber,
        Integer firstTryCorrect,
        Integer reTryCorrect,
        Integer totalFirstTry,
        Integer totalReTry
    ) {
    }

    @Builder
    public record Response(
        List<QuestionSummary> questionSummaryList
    ) {
    }
}