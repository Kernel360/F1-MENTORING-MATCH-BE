package com.biengual.userapi.schedule.application;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.biengual.userapi.mission.domain.MissionStore;
import com.biengual.userapi.schedule.domain.ScheduleService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ScheduleServiceImpl implements ScheduleService {
    private final MissionStore missionStore;

    /**
     * 미션 리셋 : 04:00 기준
     */
    @Override
    @Scheduled(cron = "0 0 4 * * *")
    public void scheduleResetMission() {
        // TODO: 미션 리셋 전에 저장해서 이후 대시보드에 보여줘야 하는지 결정되지 않음
        missionStore.resetMission();
    }
}
