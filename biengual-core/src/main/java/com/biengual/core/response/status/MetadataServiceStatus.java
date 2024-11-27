package com.biengual.core.response.status;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum MetadataServiceStatus implements ServiceStatus {
    // failure
    NOT_AGGREGATION_TABLE("U-MD-901");

    private final String code;

    @Override
    public String getServiceStatus() {
        return code;
    }
}
