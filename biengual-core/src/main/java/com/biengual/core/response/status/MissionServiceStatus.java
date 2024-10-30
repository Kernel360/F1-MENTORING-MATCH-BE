package com.biengual.core.response.status;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum MissionServiceStatus implements ServiceStatus{
    // success
    MISSION_STATUS_CHECK_SUCCESS("U-M-001"),

    // failure
    MISSION_NOT_FOUND("U-M-901")
    ;

    private final String code;

    @Override
    public String getServiceStatus() {
        return code;
    }
}
