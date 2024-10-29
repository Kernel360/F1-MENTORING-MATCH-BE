package com.biengual.userapi.core.message.response;

import org.springframework.http.HttpStatus;

import com.biengual.userapi.core.message.status.ServiceStatus;
import com.biengual.userapi.core.message.status.UserServiceStatus;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum UserResponseCode implements ResponseCode {
	USER_GET_INFO(HttpStatus.OK, UserServiceStatus.USER_GET_INFO, "유저 개인 정보 조회"),
	USER_UPDATE_INFO(HttpStatus.OK, UserServiceStatus.USER_UPDATE_INFO, "유저 개인 정보 수정"),
	USER_LOGOUT_SUCCESS(HttpStatus.OK, UserServiceStatus.USER_LOGOUT_SUCCESS, "유저 로그아웃 성공"),
	USER_STATUS_INFO(HttpStatus.OK, UserServiceStatus.USER_STATUS_INFO, "유저 로그인 상태 조회 성공")
	;

	private final HttpStatus code;
	private final ServiceStatus serviceStatus;
	private final String message;

	@Override
	public HttpStatus getStatus() {
		return code;
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
