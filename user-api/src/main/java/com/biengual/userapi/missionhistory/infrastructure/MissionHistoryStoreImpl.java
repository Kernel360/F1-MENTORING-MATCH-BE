package com.biengual.userapi.missionhistory.infrastructure;

import java.util.List;

import com.biengual.core.annotation.DataProvider;
import com.biengual.core.domain.entity.mission.MissionEntity;
import com.biengual.core.domain.entity.missionhistory.MissionHistoryEntity;
import com.biengual.userapi.mission.domain.MissionRepository;
import com.biengual.userapi.missionhistory.domain.MissionHistoryRepository;
import com.biengual.userapi.missionhistory.domain.MissionHistoryStore;

import lombok.RequiredArgsConstructor;

@DataProvider
@RequiredArgsConstructor
public class MissionHistoryStoreImpl implements MissionHistoryStore {
    private final MissionRepository missionRepository;
    private final MissionHistoryRepository missionHistoryRepository;

    @Override
    public void saveMissionsBeforeReset() {
        List<MissionEntity> missions = missionRepository.findAll();
        List<MissionHistoryEntity> histories = missions.stream()
            .map(MissionHistoryEntity::createByMissionEntity)
            .toList();
        missionHistoryRepository.saveAll(histories);
    }
}
