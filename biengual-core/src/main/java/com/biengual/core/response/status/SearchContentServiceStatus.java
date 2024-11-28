package com.biengual.core.response.status;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum SearchContentServiceStatus implements ServiceStatus {
    // failure
    SEARCH_CONTENT_SAVE_FAILED("U-SC-901"),
    SEARCH_CONTENT_DELETE_FAILED("U-SC-902"),
    OPEN_SEARCH_SERVER_ERROR("U-SC-903"),;

    private final String code;

    @Override
    public String getServiceStatus() {
        return code;
    }
}
