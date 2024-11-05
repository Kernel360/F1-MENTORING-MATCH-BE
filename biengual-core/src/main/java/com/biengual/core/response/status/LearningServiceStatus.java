package com.biengual.core.response.status;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum LearningServiceStatus implements ServiceStatus {
    // success
    LEARNING_UPDATE_LEARNING_RATE("U-L-001");

    // failure

    private final String code;

    @Override
    public String getServiceStatus() {
        return code;
    }
}
