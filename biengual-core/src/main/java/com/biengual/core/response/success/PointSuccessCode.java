package com.biengual.core.response.success;

import com.biengual.core.response.status.PointServiceStatus;
import com.biengual.core.response.status.ServiceStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public enum PointSuccessCode implements SuccessCode {
    POINT_PAYMENT_FOR_RECENT_CONTENT_SUCCESS(HttpStatus.OK, PointServiceStatus.POINT_PAYMENT_FOR_RECENT_CONTENT_SUCCESS,
        "최신 컨텐츠에 대한 포인트 지불 성공");

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
