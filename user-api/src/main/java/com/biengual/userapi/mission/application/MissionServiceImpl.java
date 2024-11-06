package com.biengual.userapi.mission.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.biengual.core.enums.PointReason;
import com.biengual.userapi.mission.domain.MissionCommand;
import com.biengual.userapi.mission.domain.MissionInfo;
import com.biengual.userapi.mission.domain.MissionReader;
import com.biengual.userapi.mission.domain.MissionService;
import com.biengual.userapi.mission.domain.MissionStore;
import com.biengual.userapi.point.domain.PointManager;
import com.biengual.userapi.validator.PointValidator;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MissionServiceImpl implements MissionService {
    private final MissionReader missionReader;
    private final MissionStore missionStore;
    private final PointManager pointManager;
    private final PointValidator pointValidator;

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
        this.updatePointByMission(command);
    }

    // Internal Methods ================================================================================================
    /**
     * 바뀐 미션 상태에 따른 검증 및 포인트 업데이트
     */
    private void updatePointByMission(MissionCommand.Update command) {
        MissionInfo.StatusInfo updatedMission = missionReader.getMissionsStatus(command.userId());

        if (pointValidator.verifyMission(updatedMission.oneContent(), command.oneContent())) {
            pointManager.updateAndSavePoint(PointReason.DAILY_CONTENT, command.userId());
        }
        if (pointValidator.verifyMission(updatedMission.bookmark(), command.bookmark())) {
            pointManager.updateAndSavePoint(PointReason.DAILY_MISSION, command.userId());
        }
        if (pointValidator.verifyMission(updatedMission.quiz(), command.quiz())) {
            pointManager.updateAndSavePoint(PointReason.DAILY_QUIZ, command.userId());
        }
    }

}
