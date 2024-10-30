package com.biengual.core.response.success;

import org.springframework.http.HttpStatus;

import com.biengual.core.response.status.MissionServiceStatus;
import com.biengual.core.response.status.ServiceStatus;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum MissionSuccessCode implements SuccessCode {
    MISSION_STATUS_CHECK_SUCCESS(HttpStatus.OK, MissionServiceStatus.MISSION_STATUS_CHECK_SUCCESS, "미션 상태 확인 성공"),
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