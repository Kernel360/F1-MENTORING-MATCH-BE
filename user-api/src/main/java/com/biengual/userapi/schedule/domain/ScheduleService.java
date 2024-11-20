package com.biengual.userapi.schedule.domain;

/**
 * 스케줄링 관련 Service
 *
 * @author 김영래
 */

public interface ScheduleService {
    void scheduleResetMission();

    void aggregateContentLevelFeedback();
}
