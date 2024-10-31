package com.biengual.userapi.schedule.application;

import static com.biengual.core.response.error.code.MissionHistoryErrorCode.*;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.biengual.core.response.error.exception.CommonException;
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
    @Transactional  // TODO: 위클리 질문
    @Scheduled(cron = "00 00 04 * * *")
    public void scheduleResetMission() {
        try {
            CompletableFuture<Void> saveTask = CompletableFuture.runAsync(missionHistoryStore::saveMissionsBeforeReset);
            saveTask.get();     // TODO: 나중에 작업량 많아 질 경우 spring batch로 전환 검토
            missionStore.resetMission();
        } catch (ExecutionException | InterruptedException e) {
            throw new CommonException(MISSION_HISTORY_SAVE_ERROR);
        }
    }
}
