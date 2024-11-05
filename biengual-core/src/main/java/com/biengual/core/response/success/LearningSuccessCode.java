package com.biengual.core.response.success;

import com.biengual.core.response.status.LearningServiceStatus;
import com.biengual.core.response.status.ServiceStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public enum LearningSuccessCode implements SuccessCode {
    LEARNING_UPDATE_LEARNING_RATE(HttpStatus.OK, LearningServiceStatus.LEARNING_UPDATE_LEARNING_RATE,
        "학습률 업데이트 성공");

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
