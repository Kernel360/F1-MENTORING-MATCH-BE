package com.biengual.core.response.status;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum RecommenderServiceStatus implements ServiceStatus {
    // success
    RECOMMENDER_UPDATE_SUCCESS("U-R-001"),
    RECOMMENDER_CATEGORY_VIEW_SUCCESS("U-R-002");

    private final String code;

    @Override
    public String getServiceStatus() {
        return code;
    }
}
