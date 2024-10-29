package com.biengual.core.response.error.code;

import org.springframework.http.HttpStatus;

import com.biengual.core.response.status.ServiceStatus;
import com.biengual.core.response.status.TokenServiceStatus;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum TokenErrorCode implements ErrorCode {
	REFRESH_TOKEN_NOT_FOUND(
		HttpStatus.NOT_FOUND, TokenServiceStatus.REFRESH_TOKEN_NOT_FOUND, "리프레시 토큰 확인 불가"
	),
	TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, TokenServiceStatus.TOKEN_EXPIRED, "토큰 만료"),
	TOKEN_INVALID(HttpStatus.UNAUTHORIZED, TokenServiceStatus.TOKEN_INVALID, "유효하지 않은 토큰")
	;

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
