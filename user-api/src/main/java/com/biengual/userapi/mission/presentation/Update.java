package com.biengual.userapi.mission.presentation;

public class Update {

    public record Request(
        boolean oneContent,
        boolean memo,
        boolean quiz
    ) {
    }
}
