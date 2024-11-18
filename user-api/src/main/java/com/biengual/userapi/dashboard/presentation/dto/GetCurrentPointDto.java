package com.biengual.userapi.dashboard.presentation.dto;

public class GetCurrentPointDto {

    public record Response(
        Long currentPoint
    ) {
    }
}
