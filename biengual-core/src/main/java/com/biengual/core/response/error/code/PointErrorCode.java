package com.biengual.core.response.error.code;

import org.springframework.http.HttpStatus;

import com.biengual.core.response.status.PointServiceStatus;
import com.biengual.core.response.status.ServiceStatus;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum PointErrorCode implements ErrorCode {
    POINT_NOT_FOUND(
        HttpStatus.NOT_FOUND, PointServiceStatus.POINT_NOT_FOUND, "포인트 조회 실패"
    ),
    POINT_NEVER_MINUS(
        HttpStatus.FORBIDDEN, PointServiceStatus.POINT_NEVER_MINUS, "포인트 부족"
    ),
    POINTLESS_CONTENT(HttpStatus.BAD_REQUEST,
        PointServiceStatus.POINTLESS_CONTENT, "포인트 지불이 필요없는 컨텐츠")
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
