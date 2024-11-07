package com.biengual.userapi.mission.infrastructure;

import com.biengual.core.annotation.DataProvider;
import com.biengual.userapi.mission.domain.MissionCommand;
import com.biengual.userapi.mission.domain.MissionCustomRepository;
import com.biengual.userapi.mission.domain.MissionStore;

import lombok.RequiredArgsConstructor;

@DataProvider
@RequiredArgsConstructor
public class MissionStoreImpl implements MissionStore {
    private final MissionCustomRepository missionCustomRepository;

    // 스케줄러로 4시마다 미션 리셋
    @Override
    public void resetMission() {
        missionCustomRepository.resetMission();
    }

    // 미션 완료 업데이트
    @Override
    public void updateMissionComplete(MissionCommand.Update command) {
        missionCustomRepository.completeMission(command);
    }
}
