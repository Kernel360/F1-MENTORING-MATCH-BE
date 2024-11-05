package com.biengual.core.response.error.code;

import org.springframework.http.HttpStatus;

import com.biengual.core.response.status.PointHistoryServiceStatus;
import com.biengual.core.response.status.ServiceStatus;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum PointHistoryErrorCode implements ErrorCode {
    POINT_HISTORY_NOT_FOUND(
        HttpStatus.NOT_FOUND, PointHistoryServiceStatus.POINT_HISTORY_NOT_FOUND, "포인트 내역 조회 실패"
    );

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
