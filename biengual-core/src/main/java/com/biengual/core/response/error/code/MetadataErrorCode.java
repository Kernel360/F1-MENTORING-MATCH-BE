package com.biengual.core.response.error.code;

import com.biengual.core.response.status.MetadataServiceStatus;
import com.biengual.core.response.status.ServiceStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public enum MetadataErrorCode implements ErrorCode {
    NOT_AGGREGATION_TABLE(HttpStatus.INTERNAL_SERVER_ERROR, MetadataServiceStatus.NOT_AGGREGATION_TABLE,
        "집계하지 않는 테이블");

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
