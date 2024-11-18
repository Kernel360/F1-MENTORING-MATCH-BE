package com.biengual.userapi.content.presentation.dto;

import com.biengual.core.enums.ContentLevel;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import static com.biengual.core.constant.BadRequestMessageConstant.NULL_CONTENT_ID_ERROR_MESSAGE;
import static com.biengual.core.constant.BadRequestMessageConstant.NULL_CONTENT_LEVEL_ERROR_MESSAGE;

public class SubmitLevelFeedbackDto {

    public record Request(
        @NotNull(message = NULL_CONTENT_ID_ERROR_MESSAGE)
        Long contentId,
        @NotNull(message = NULL_CONTENT_LEVEL_ERROR_MESSAGE)
        ContentLevel contentLevel
    ) {
    }

    @Builder
    public record Response() {
    }
}