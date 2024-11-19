package com.biengual.userapi.schedule.application;

import com.biengual.userapi.metadata.domain.MetadataStore;
import com.biengual.userapi.mission.domain.MissionStore;
import com.biengual.userapi.missionhistory.domain.MissionHistoryStore;
import com.biengual.userapi.schedule.domain.ScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ScheduleServiceImpl implements ScheduleService {
    private final MissionStore missionStore;
    private final MissionHistoryStore missionHistoryStore;
    private final MetadataStore metadataStore;

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

    // TODO: 나중에 같은 시간에 실행하는 집계 관련 스케줄러는
    //  aggregationMetadata의 모든 데이터를 가져와 한번에 실행할 수 있을 것 같습니다.
    @Override
    @Transactional
    @Scheduled(cron = "00 00 04 * * *")
    public void aggregateContentLevelFeedback() {
        metadataStore.aggregateContentLevelFeedbackHistory();
    }
}
