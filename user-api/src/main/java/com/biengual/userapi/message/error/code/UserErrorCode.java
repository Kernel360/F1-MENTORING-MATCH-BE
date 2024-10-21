package com.biengual.userapi.message.error.code;

import org.springframework.http.HttpStatus;

import com.biengual.userapi.message.status.ServiceStatus;
import com.biengual.userapi.message.status.UserServiceStatus;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum UserErrorCode implements ErrorCode {
	USER_NOT_FOUND(
		HttpStatus.NOT_FOUND, UserServiceStatus.USER_NOT_FOUND, "유저를 찾을 수 없습니다."
	),
	USER_FAIL_DEACTIVATE(
		HttpStatus.BAD_REQUEST, UserServiceStatus.USER_FAIL_DEACTIVATE, "비활성화된 유저입니다."
	),
	USER_FAIL_SUSPEND(
		HttpStatus.BAD_REQUEST, UserServiceStatus.USER_FAIL_SUSPEND, "정지된 유저입니다."
	),
	USER_PERMISSION_DENIED(
		HttpStatus.FORBIDDEN, UserServiceStatus.USER_PERMISSION_DENIED, "접근 권한이 없는 유저입니다."
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
