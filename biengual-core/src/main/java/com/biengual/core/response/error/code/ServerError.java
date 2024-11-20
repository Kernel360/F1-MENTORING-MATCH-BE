package com.biengual.core.response.error.code;

import com.biengual.core.response.status.ServerServiceStatus;
import com.biengual.core.response.status.ServiceStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

// TODO: Core의 Util에서 발생한 예외를 위한 ErrorCode 여기 있는게 맞는지?
@RequiredArgsConstructor
public enum ServerError implements ErrorCode {
    SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, ServerServiceStatus.SERVER_ERROR, "서버 에러"),
    TIME_RANGE_IS_INVALID(HttpStatus.INTERNAL_SERVER_ERROR, ServerServiceStatus.TIME_RANGE_IS_INVALID,
        "유효하지 않은 시간 범위"),
    INTERVAL_TYPE_IS_INVALID(HttpStatus.INTERNAL_SERVER_ERROR, ServerServiceStatus.INTERVAL_TYPE_IS_INVALID,
        "유효하지 않은 Interval Type");

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
