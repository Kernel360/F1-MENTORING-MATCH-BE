package com.biengual.userapi.mission.domain;

public interface MissionStore {
    void resetMission();

    void updateMissionComplete(MissionCommand.Update command);
}
