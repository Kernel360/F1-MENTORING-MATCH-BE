package com.biengual.userapi.mission.presentation;

public class Status {

    public record Response(
        boolean oneContent,
        boolean memo,
        boolean quiz,
        Integer count
    ) {
    }
}
