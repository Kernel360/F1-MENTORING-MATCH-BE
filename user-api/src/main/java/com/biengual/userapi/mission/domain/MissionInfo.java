package com.biengual.userapi.mission.domain;

public class MissionInfo {

    public record StatusInfo(
        boolean oneContent,
        boolean bookmark,
        boolean quiz,
        Integer count
    ) {
    }
}
