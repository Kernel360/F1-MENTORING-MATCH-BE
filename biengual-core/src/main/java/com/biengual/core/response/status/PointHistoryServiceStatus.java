package com.biengual.core.response.status;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum PointHistoryServiceStatus implements ServiceStatus {
    // success


    // failure
    POINT_HISTORY_NOT_FOUND("U-PH-901")
    ;

    private final String code;

    @Override
    public String getServiceStatus() {
        return code;
    }
}
