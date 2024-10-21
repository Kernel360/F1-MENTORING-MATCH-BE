package com.biengual.userapi.message.status;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum UserServiceStatus implements ServiceStatus{
	// success
	USER_INPUT_INFO("U-U-001"),
	USER_GET_INFO("U-U-002"),
	USER_UPDATE_INFO("U-U-003"),
	USER_LOGIN_SUCCESS("U-U-005"),
	USER_LOGOUT_SUCCESS("U-U-006"),
	USER_STATUS_INFO("U-U-007"),

	// failure
	USER_NOT_FOUND("U-U-901"),
	USER_FAIL_DEACTIVATE("U-U-902"),
	USER_FAIL_SUSPEND("U-U-903"),
	USER_PERMISSION_DENIED("U-U-904");

	private final String code;

	@Override
	public String getServiceStatus() {
		return code;
	}
}
