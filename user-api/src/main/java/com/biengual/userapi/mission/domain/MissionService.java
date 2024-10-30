package com.biengual.userapi.mission.domain;

public interface MissionService {
    MissionInfo.StatusInfo getMissionStatus(Long userId);

    void updateMissionComplete(MissionCommand.Update command);

    void resetMission();
}
