package com.biengual.userapi.mission.presentation;

public class StatusDto {
    public record Response(
        boolean oneContent,
        boolean bookmark,
        boolean quiz,
        Integer count
    ) {
    }
}
