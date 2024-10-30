package com.biengual.userapi.mission.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.biengual.userapi.mission.domain.MissionCommand;
import com.biengual.userapi.mission.domain.MissionInfo;
import com.biengual.userapi.mission.domain.MissionReader;
import com.biengual.userapi.mission.domain.MissionService;
import com.biengual.userapi.mission.domain.MissionStore;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MissionServiceImpl implements MissionService {
    private final MissionReader missionReader;
    private final MissionStore missionStore;

    /**
     * 미션 상태 조회
     */
    @Override
    @Transactional(readOnly = true)
    public MissionInfo.StatusInfo getMissionStatus(Long userId) {
        return missionReader.getMissionsStatus(userId);
    }

    /**
     * 미션 상태 업데이트
     */
    @Override
    @Transactional
    public void updateMissionComplete(MissionCommand.Update command) {
        missionStore.updateMissionComplete(command);
    }

}
