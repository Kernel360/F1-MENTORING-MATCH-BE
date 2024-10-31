package com.biengual.userapi.mission.presentation;

import static com.biengual.core.constant.BadRequestMessageConstant.*;

import jakarta.validation.constraints.NotNull;

public class UpdateDto {

    public record Request(
        @NotNull(message = NULL_MISSION_LIST_ERROR_MESSAGE)
        boolean oneContent,
        @NotNull(message = NULL_MISSION_LIST_ERROR_MESSAGE)
        boolean bookmark,
        @NotNull(message = NULL_MISSION_LIST_ERROR_MESSAGE)
        boolean quiz
    ) {
    }
}
