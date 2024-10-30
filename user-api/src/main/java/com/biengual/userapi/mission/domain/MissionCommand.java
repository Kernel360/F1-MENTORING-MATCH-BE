package com.biengual.userapi.mission.domain;

public class MissionCommand {

    public record Update(
        Long userId,
        boolean oneContent,
        boolean bookmark,
        boolean quiz
    ) {
    }
}
