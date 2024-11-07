package com.biengual.userapi.missionhistory.presentation;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Builder;

public class RecentHistoryDto {
    public record Response(
        LocalDateTime date,
        Integer count
    ) {
    }

    @Builder

    public record Responses(
        List<Response> recentHistories
    ) {
    }
}
