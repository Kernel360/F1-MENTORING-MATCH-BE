package com.biengual.core.response.status;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum MissionHistoryServiceStatus implements ServiceStatus {
    // success

    // failure,
    MISSION_HISTORY_SAVE_ERROR("U-MH-901");

    private final String code;

    @Override
    public String getServiceStatus() {
        return code;
    }
}
