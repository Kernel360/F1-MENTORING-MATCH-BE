package com.biengual.core.response.error.code;

import org.springframework.http.HttpStatus;

import com.biengual.core.response.status.ImageServiceStatus;
import com.biengual.core.response.status.ServiceStatus;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum ImageErrorCode implements ErrorCode {
    IMAGE_STORE_FAILURE(
        HttpStatus.CONFLICT, ImageServiceStatus.IMAGE_STORE_FAILURE, "S3 이미지 저장 실패"
    ),
    IMAGE_READ_FAILURE(
        HttpStatus.NOT_FOUND, ImageServiceStatus.IMAGE_READ_FAILURE, "S3 이미지 조회 실패"
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
