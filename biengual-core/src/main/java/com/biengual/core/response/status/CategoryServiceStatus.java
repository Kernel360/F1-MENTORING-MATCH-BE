package com.biengual.core.response.status;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum CategoryServiceStatus implements ServiceStatus {
    // success
    CATEGORY_FOUND_SUCCESS("U-CG-001"),

    // error
    CATEGORY_NOT_FOUND("U-CG-901");

    private final String code;

    @Override
    public String getServiceStatus() {
        return code;
    }
}
