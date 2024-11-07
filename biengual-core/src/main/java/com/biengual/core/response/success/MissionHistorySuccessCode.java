package com.biengual.core.response.success;

import org.springframework.http.HttpStatus;

import com.biengual.core.response.status.MissionHistoryServiceStatus;
import com.biengual.core.response.status.ServiceStatus;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum MissionHistorySuccessCode implements SuccessCode {
    MISSION_RECENT_HISTORY_VIEW_SUCCESS(HttpStatus.OK, MissionHistoryServiceStatus.MISSION_RECENT_HISTORY_VIEW_SUCCESS, "미션 최근 기록 조회 성공"),
    ;

    private final HttpStatus code;
    private final ServiceStatus serviceStatus;
    private final String message;

    @Override
    public HttpStatus getStatus() {
        return code;
    }

    @Override
    public String getCode() {
        return serviceStatus.getServiceStatus();
    }

    @Override
    public String getMessage() {
        return message;
    }
}