package com.biengual.userapi.mission.domain;

public interface MissionStore {
    void createMission(Long userId);

    void resetMission(Long userId);
}
