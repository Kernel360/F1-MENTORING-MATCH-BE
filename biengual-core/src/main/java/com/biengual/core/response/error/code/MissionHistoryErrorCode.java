package com.biengual.core.response.error.code;

import org.springframework.http.HttpStatus;

import com.biengual.core.response.status.MissionHistoryServiceStatus;
import com.biengual.core.response.status.ServiceStatus;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum MissionHistoryErrorCode implements ErrorCode {
    MISSION_HISTORY_SAVE_ERROR(HttpStatus.REQUEST_TIMEOUT, MissionHistoryServiceStatus.MISSION_HISTORY_SAVE_ERROR, "미션 기록 저장 실패")
    ;

    private final HttpStatus httpStatus;
    private final ServiceStatus serviceStatus;
    private final String message;

    @Override
    public HttpStatus getStatus() {
        return httpStatus;
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
