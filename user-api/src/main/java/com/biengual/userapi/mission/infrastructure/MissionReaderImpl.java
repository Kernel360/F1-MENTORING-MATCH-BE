package com.biengual.userapi.mission.infrastructure;

import static com.biengual.core.response.error.code.MissionErrorCode.*;

import com.biengual.core.annotation.DataProvider;
import com.biengual.core.domain.entity.mission.MissionEntity;
import com.biengual.core.response.error.exception.CommonException;
import com.biengual.userapi.mission.domain.MissionCustomRepository;
import com.biengual.userapi.mission.domain.MissionInfo;
import com.biengual.userapi.mission.domain.MissionReader;
import com.biengual.userapi.mission.domain.MissionRepository;

import lombok.RequiredArgsConstructor;

@DataProvider
@RequiredArgsConstructor
public class MissionReaderImpl implements MissionReader {
    private final MissionCustomRepository missionCustomRepository;
    private final MissionRepository missionRepository;

    // userId(PK) 로 미션 상태 조회
    @Override
    public MissionInfo.StatusInfo getMissionsStatus(Long userId) {
        return missionCustomRepository.findMissionStatusByUserId(userId)
            .orElseThrow(() -> new CommonException(MISSION_NOT_FOUND));
    }

    // userId(PK) 로 미션 조회
    @Override
    public void findMission(Long userId) {
        missionRepository.findById(userId)
            .orElseGet(() -> {
                    MissionEntity mission = MissionEntity.createByUserId(userId);
                    return missionRepository.save(mission);
                }
            );
    }

}
