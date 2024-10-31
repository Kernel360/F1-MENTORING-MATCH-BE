package com.biengual.userapi.mission.infrastructure;

import com.biengual.core.annotation.DataProvider;
import com.biengual.core.domain.entity.mission.MissionEntity;
import com.biengual.userapi.mission.domain.MissionCommand;
import com.biengual.userapi.mission.domain.MissionCustomRepository;
import com.biengual.userapi.mission.domain.MissionRepository;
import com.biengual.userapi.mission.domain.MissionStore;

import lombok.RequiredArgsConstructor;

@DataProvider
@RequiredArgsConstructor
public class MissionStoreImpl implements MissionStore {
    private final MissionRepository missionRepository;
    private final MissionCustomRepository missionCustomRepository;

    @Override
    public void createMission(Long userId) {
        MissionEntity mission = MissionEntity.createByUserId(userId);

        missionRepository.save(mission);
    }

    @Override
    public void resetMission() {
        missionCustomRepository.resetMission();
    }

    @Override
    public void updateMissionComplete(MissionCommand.Update command) {
        missionCustomRepository.completeMission(command);
    }
}
