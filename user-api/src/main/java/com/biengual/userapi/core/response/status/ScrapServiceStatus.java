package com.biengual.userapi.core.response.status;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum ScrapServiceStatus implements ServiceStatus {
	// success
	SCRAP_VIEW_SUCCESS("U-S-001"),
	SCRAP_CREATE_SUCCESS("U-S-002"),
	SCRAP_DELETE_SUCCESS("U-S-003"),
	SCRAP_CHECK_SUCCESS("U-S-004"),

	// failure,
	SCRAP_NOT_FOUND("U-S-901"),
	SCRAP_ALREADY_EXISTS("U-S-902");

	private final String code;

	@Override
	public String getServiceStatus() {
		return code;
	}
}
