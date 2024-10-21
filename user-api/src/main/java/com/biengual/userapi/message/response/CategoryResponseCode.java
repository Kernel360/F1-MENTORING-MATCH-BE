package com.biengual.userapi.message.response;

import com.biengual.userapi.message.status.CategoryServiceStatus;
import com.biengual.userapi.message.status.ServiceStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public enum CategoryResponseCode implements ResponseCode {
    CATEGORY_FOUND_SUCCESS(HttpStatus.OK, CategoryServiceStatus.CATEGORY_FOUND_SUCCESS, "모든 카테고리 조회 성공");

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
