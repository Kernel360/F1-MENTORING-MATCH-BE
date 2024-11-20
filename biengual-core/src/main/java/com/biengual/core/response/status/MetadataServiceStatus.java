package com.biengual.core.response.status;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum MetadataServiceStatus implements ServiceStatus {
    // failure
    NOT_AGGREGATION_TABLE("U-M-901");

    private final String code;

    @Override
    public String getServiceStatus() {
        return code;
    }
}
