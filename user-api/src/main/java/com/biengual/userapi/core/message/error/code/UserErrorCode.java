package com.biengual.userapi.core.message.error.code;

import org.springframework.http.HttpStatus;

import com.biengual.userapi.core.message.status.ServiceStatus;
import com.biengual.userapi.core.message.status.UserServiceStatus;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum UserErrorCode implements ErrorCode {
	USER_NOT_FOUND(
		HttpStatus.NOT_FOUND, UserServiceStatus.USER_NOT_FOUND, "유저 조회 실패"
	),
	USER_FAIL_DEACTIVATE(
		HttpStatus.BAD_REQUEST, UserServiceStatus.USER_FAIL_DEACTIVATE, "비활성화된 유저"
	),
	USER_FAIL_SUSPEND(
		HttpStatus.BAD_REQUEST, UserServiceStatus.USER_FAIL_SUSPEND, "정지된 유저"
	),
	USER_PERMISSION_DENIED(
		HttpStatus.FORBIDDEN, UserServiceStatus.USER_PERMISSION_DENIED, "잘못된 접근 권한"
	);

	private final HttpStatus httpStatus;
	private final ServiceStatus serviceStatus;
	private final String message;

	@Override
	public HttpStatus getStatus() {
		return httpStatus;
	}

	@Override
	public String getCode() {
		return serviceStatus.getServiceStatus();
	}

	@Override
	public String getMessage() {
		return message;
	}
}
