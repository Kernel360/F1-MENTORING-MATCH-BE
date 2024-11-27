package com.biengual.core.response.status;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum PointServiceStatus implements ServiceStatus {
    // success
    POINT_PAYMENT_FOR_RECENT_CONTENT_SUCCESS("U-P-001"),

    // failure
    POINT_NOT_FOUND("U-P-901"),
    POINT_NEVER_MINUS("U-P-902"),
    CONTENT_NOT_RECENT("U-P-903"),
    ALREADY_PAID_FOR_RECENT_CONTENT("U-P-904")
    ;

    private final String code;

    @Override
    public String getServiceStatus() {
        return code;
    }
}
