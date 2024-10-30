package com.biengual.userapi.mission.presentation;

import jakarta.validation.constraints.NotNull;

public class UpdateDto {

    public record Request(
        @NotNull
        boolean oneContent,
        @NotNull
        boolean memo,
        @NotNull
        boolean quiz
    ) {
    }
}
