package com.biengual.core.response.error.code;

import org.springframework.http.HttpStatus;

import com.biengual.core.response.status.MissionServiceStatus;
import com.biengual.core.response.status.ServiceStatus;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum MissionErrorCode implements ErrorCode {
    MISSION_NOT_FOUND(
        HttpStatus.NOT_FOUND, MissionServiceStatus.MISSION_NOT_FOUND, "미션 조회 실패"
    )
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
