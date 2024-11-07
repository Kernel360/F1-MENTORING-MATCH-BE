package com.biengual.userapi.schedule.application;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.biengual.userapi.mission.domain.MissionStore;
import com.biengual.userapi.missionhistory.domain.MissionHistoryStore;
import com.biengual.userapi.schedule.domain.ScheduleService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ScheduleServiceImpl implements ScheduleService {
    private final MissionStore missionStore;
    private final MissionHistoryStore missionHistoryStore;

    /**
     * 미션 리셋 : 04:00 기준
     */
    @Override
    @Transactional
    @Scheduled(cron = "00 00 04 * * *")
    public void scheduleResetMission() {
        missionHistoryStore.saveMissionsBeforeReset();
        missionStore.resetMission();
    }
}
