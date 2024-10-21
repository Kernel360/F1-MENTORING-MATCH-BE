package com.biengual.userapi.message.status;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum TokenServiceStatus implements ServiceStatus {
	// success

	// failure
	REFRESH_TOKEN_NOT_FOUND("U-T-901"),
	TOKEN_EXPIRED("U-T-902"),
	TOKEN_INVALID("U-T-903")
	;
	private final String code;

	@Override
	public String getServiceStatus() {
		return code;
	}
}
