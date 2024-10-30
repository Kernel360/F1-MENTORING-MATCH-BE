package com.biengual.userapi.mission.domain;

public class MissionCommand {

    public record Update(
        Long missionId,
        boolean oneContent,
        boolean memo,
        boolean quiz
    ) {
    }
}
