package com.biengual.userapi.core.response.success;

import org.springframework.http.HttpStatus;

import com.biengual.userapi.core.response.status.ScrapServiceStatus;
import com.biengual.userapi.core.response.status.ServiceStatus;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum ScrapSuccessCode implements SuccessCode {
	SCRAP_VIEW_SUCCESS(HttpStatus.OK, ScrapServiceStatus.SCRAP_VIEW_SUCCESS, "스크랩 조회 성공"),
	SCRAP_CREATE_SUCCESS(HttpStatus.CREATED, ScrapServiceStatus.SCRAP_CREATE_SUCCESS, "스크랩 생성 성공"),
	SCRAP_DELETE_SUCCESS(HttpStatus.OK, ScrapServiceStatus.SCRAP_DELETE_SUCCESS, "스크랩 삭제 성공"),
	SCRAP_CHECK_SUCCESS(HttpStatus.OK, ScrapServiceStatus.SCRAP_CHECK_SUCCESS, "스크랩 확인 요청 성공");

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
