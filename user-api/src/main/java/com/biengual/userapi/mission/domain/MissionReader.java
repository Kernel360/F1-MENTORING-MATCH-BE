package com.biengual.userapi.mission.domain;

public interface MissionReader {
    MissionInfo.StatusInfo getMissionsStatus(Long userId);
}
