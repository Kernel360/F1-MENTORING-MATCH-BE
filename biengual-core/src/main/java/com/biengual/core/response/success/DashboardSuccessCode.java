package com.biengual.core.response.success;

import org.springframework.http.HttpStatus;

import com.biengual.core.response.status.DashboardServiceStatus;
import com.biengual.core.response.status.ServiceStatus;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum DashboardSuccessCode implements SuccessCode {
    RECENT_LEARNING_SUMMARY_VIEW_SUCCESS(HttpStatus.OK, DashboardServiceStatus.RECENT_LEARNING_SUMMARY_VIEW_SUCCESS,
        "최근 학습 컨텐츠 1개 조회 성공"),
    RECENT_LEARNING_VIEW_SUCCESS(HttpStatus.OK, DashboardServiceStatus.RECENT_LEARNING_VIEW_SUCCESS,
        "최근 학습 조회 성공"),
    CATEGORY_LEARNING_VIEW_SUCCESS(HttpStatus.OK, DashboardServiceStatus.CATEGORY_LEARNING_VIEW_SUCCESS,
        "카테고리별 학습 조회 성공"),
    CURRENT_POINT_VIEW_SUCCESS(HttpStatus.OK, DashboardServiceStatus.CURRENT_POINT_VIEW_SUCCESS,
        "현재 포인트 조회 성공"),
    MISSION_CALENDAR_VIEW_SUCCESS(HttpStatus.OK, DashboardServiceStatus.MISSION_CALENDAR_VIEW_SUCCESS,
        "월간 미션 달력 조회 성공"),
    MONTHLY_POINT_HISTORY_VIEW_SUCCESS(HttpStatus.OK, DashboardServiceStatus.MONTHLY_POINT_HISTORY_VIEW_SUCCESS,
        "월간 포인트 내역 조회 성공"),
    MONTHLY_QUIZ_HISTORY_VIEW_SUCCESS(HttpStatus.OK, DashboardServiceStatus.MONTHLY_QUIZ_HISTORY_VIEW_SUCCESS,
        "월간 퀴즈 내역 조회 성공");

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
