package com.biengual.userapi.mission.infrastructure;

import static com.biengual.core.response.error.code.MissionErrorCode.*;

import com.biengual.core.annotation.DataProvider;
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

    @Override
    public MissionInfo.StatusInfo getMissionsStatus(Long userId) {
        return missionCustomRepository.findMissionStatusByUserId(userId)
            .orElseThrow(() -> new CommonException(MISSION_NOT_FOUND));
    }

    @Override
    public boolean existsMission(Long userId) {
        return missionRepository.existsById(userId);
    }

    @Override
    public boolean checkMissionDate(Long userId) {
        return missionCustomRepository.checkMissionDate(userId);
    }
}
