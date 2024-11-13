package com.biengual.userapi.question.presentation;

public class VerifyDto {

    public record Request(
        String questionId,
        String answer
    ) {

    }
}
