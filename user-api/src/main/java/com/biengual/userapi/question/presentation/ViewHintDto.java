package com.biengual.userapi.question.presentation;

public class ViewHintDto {

    public record Request(
        String questionId
    ) {
    }

    public record Response(
        String hint
    ) {
    }
}
