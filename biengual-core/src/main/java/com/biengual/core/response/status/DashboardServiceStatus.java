package com.biengual.core.response.status;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum DashboardServiceStatus implements ServiceStatus {
    // success
    RECENT_LEARNING_SUMMARY_VIEW_SUCCESS("U-D-001"),
    RECENT_LEARNING_VIEW_SUCCESS("U-D-002"),
    CATEGORY_LEARNING_VIEW_SUCCESS("U-D-003"),
    CURRENT_POINT_VIEW_SUCCESS("U-D-004"),
    MISSION_CALENDAR_VIEW_SUCCESS("U-D-005"),
    MONTHLY_POINT_HISTORY_VIEW_SUCCESS("U-D-006");

    // failure

    private final String code;

    @Override
    public String getServiceStatus() {
        return code;
    }
}
