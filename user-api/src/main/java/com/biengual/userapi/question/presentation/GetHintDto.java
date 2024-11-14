package com.biengual.userapi.question.presentation;

public class GetHintDto {

    public record Request(
        String questionId
    ) {
    }

    public record Response(
        String hint
    ) {
    }
}
