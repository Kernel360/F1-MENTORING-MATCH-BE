package com.biengual.core.response.status;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum ServerServiceStatus implements ServiceStatus {
    // failure
    SERVER_ERROR("U-SV-901"),
    TIME_RANGE_IS_INVALID("U-SV-902"),
    INTERVAL_TYPE_IS_INVALID("U-SV-903");

    private final String code;

    @Override
    public String getServiceStatus() {
        return code;
    }
}
