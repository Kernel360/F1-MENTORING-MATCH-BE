package com.biengual.core.response.status;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum S3ServiceStatus implements ServiceStatus {
    // success
    S3_STORE_SUCCESS("U-S3-001"),
    S3_READ_SUCCESS("U-S3-002"),

    // failure
    S3_STORE_FAILURE("U-S3-901"),
    S3_READ_FAILURE("U-S3-902");

    private final String code;

    @Override
    public String getServiceStatus() {
        return code;
    }
}
