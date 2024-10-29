package com.biengual.userapi.core.response.error.code;

import com.biengual.userapi.core.response.status.ServerServiceStatus;
import com.biengual.userapi.core.response.status.ServiceStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public enum ServerError implements ErrorCode {
    SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, ServerServiceStatus.SERVER_ERROR, "서버 에러");

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
