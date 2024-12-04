package com.biengual.core.response.status;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum ImageServiceStatus implements ServiceStatus {
    // success
    IMAGE_STORE_SUCCESS("U-I-001"),
    IMAGE_READ_SUCCESS("U-I-002"),

    // failure
    IMAGE_STORE_FAILURE("U-I-901"),
    IMAGE_READ_FAILURE("U-I-902");

    private final String code;

    @Override
    public String getServiceStatus() {
        return code;
    }
}
