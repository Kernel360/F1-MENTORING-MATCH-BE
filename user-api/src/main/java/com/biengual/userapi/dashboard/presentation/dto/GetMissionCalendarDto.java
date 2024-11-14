package com.biengual.userapi.dashboard.presentation.dto;

import lombok.Builder;

import java.time.LocalDate;
import java.util.List;

public class GetMissionCalendarDto {

    public record MissionStatus(
        Boolean oneContent,
        Boolean bookmark,
        Boolean quiz,
        Integer count
    ) {
    }

    public record MissionHistory(
        LocalDate date,
        MissionStatus missionStatus
    ) {
    }


    @Builder
    public record Response(
        List<MissionHistory> monthlyHistoryList
    ) {
    }
}
