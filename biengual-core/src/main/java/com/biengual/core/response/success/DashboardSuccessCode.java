package com.biengual.core.response.success;

import com.biengual.core.response.status.DashboardServiceStatus;
import com.biengual.core.response.status.ServiceStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public enum DashboardSuccessCode implements SuccessCode {
    RECENT_LEARNING_SUMMARY_VIEW_SUCCESS(HttpStatus.OK, DashboardServiceStatus.RECENT_LEARNING_SUMMARY_VIEW_SUCCESS,
        "최근 학습 컨텐츠 1개 조회 성공"),
    RECENT_LEARNING_VIEW_SUCCESS(HttpStatus.OK, DashboardServiceStatus.RECENT_LEARNING_VIEW_SUCCESS,
        "최근 학습 조회 성공"),
    CATEGORY_LEARNING_VIEW_SUCCESS(HttpStatus.OK, DashboardServiceStatus.CATEGORY_LEARNING_VIEW_SUCCESS,
        "카테고리별 학습 조회 성공");

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
