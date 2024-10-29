package com.biengual.core.response.status;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum RequestServiceStatus implements ServiceStatus {
    // failure
    BAD_REQUEST("U-BR-901");

    private final String code;

    @Override
    public String getServiceStatus() {
        return code;
    }
}
