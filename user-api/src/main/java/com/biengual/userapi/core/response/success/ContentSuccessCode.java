package com.biengual.userapi.core.response.success;

import org.springframework.http.HttpStatus;

import com.biengual.userapi.core.response.status.ContentServiceStatus;
import com.biengual.userapi.core.response.status.ServiceStatus;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum ContentSuccessCode implements SuccessCode {
	CONTENT_CREATE_SUCCESS(HttpStatus.CREATED, ContentServiceStatus.CONTENT_CREATE_SUCCESS, "컨텐츠 생성 성공"),
	CONTENT_VIEW_SUCCESS(HttpStatus.OK, ContentServiceStatus.CONTENT_VIEW_SUCCESS, "컨텐츠 조회 성공"),
	CONTENT_MODIFY_SUCCESS(HttpStatus.OK, ContentServiceStatus.CONTENT_MODIFY_SUCCESS, "컨텐츠 상태 변경 성공"),
	CONTENT_DEACTIVATE_SUCCESS(HttpStatus.OK, ContentServiceStatus.CONTENT_DEACTIVATE_SUCCESS, "컨텐츠 비활성화 성공")
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
