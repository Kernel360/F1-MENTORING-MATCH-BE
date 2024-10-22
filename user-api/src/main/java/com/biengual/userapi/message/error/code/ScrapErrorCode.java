package com.biengual.userapi.message.error.code;

import org.springframework.http.HttpStatus;

import com.biengual.userapi.message.status.ScrapServiceStatus;
import com.biengual.userapi.message.status.ServiceStatus;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum ScrapErrorCode implements ErrorCode {
	SCRAP_NOT_FOUND(HttpStatus.NOT_FOUND, ScrapServiceStatus.SCRAP_NOT_FOUND, "스크랩 조회 실패"),
	SCRAP_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, ScrapServiceStatus.SCRAP_ALREADY_EXISTS, "이미 등록된 스크랩 요청")
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
