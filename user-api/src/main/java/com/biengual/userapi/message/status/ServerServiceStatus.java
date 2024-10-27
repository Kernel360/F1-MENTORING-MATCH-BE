package com.biengual.userapi.message.status;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum ServerServiceStatus implements ServiceStatus {
    // failure
    SERVER_ERROR("U-SV-901");

    private final String code;

    @Override
    public String getServiceStatus() {
        return code;
    }
}
