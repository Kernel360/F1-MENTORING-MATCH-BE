package com.biengual.core.response.success;

import org.springframework.http.HttpStatus;

import com.biengual.core.response.status.S3ServiceStatus;
import com.biengual.core.response.status.ServiceStatus;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum S3SuccessCode implements SuccessCode {
    S3_STORE_SUCCESS(HttpStatus.CREATED, S3ServiceStatus.S3_STORE_SUCCESS, "S3 이미지 저장 성공"),
    S3_READ_SUCCESS(HttpStatus.OK, S3ServiceStatus.S3_READ_SUCCESS, "S3 이미지 조회 성공");

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
