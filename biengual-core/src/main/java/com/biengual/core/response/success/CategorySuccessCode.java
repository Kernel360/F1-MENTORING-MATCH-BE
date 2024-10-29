package com.biengual.core.response.success;

import com.biengual.core.response.status.CategoryServiceStatus;
import com.biengual.core.response.status.ServiceStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public enum CategorySuccessCode implements SuccessCode {
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
