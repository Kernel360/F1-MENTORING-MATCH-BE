package com.echall.platform.message.response;

import org.springframework.http.HttpStatus;

import com.echall.platform.message.status.ContentServiceStatus;
import com.echall.platform.message.status.ServiceStatus;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum ContentResponseCode implements ResponseCode {
	CONTENT_CREATE_SUCCESS(HttpStatus.OK, ContentServiceStatus.CONTENT_CREATE_SUCCESS, "컨텐츠 생성 성공"),
	CONTENT_VIEW_SUCCESS(HttpStatus.OK, ContentServiceStatus.CONTENT_VIEW_SUCCESS, "컨텐츠 조회 성공"),
	CONTENT_MODIFY_SUCCESS(HttpStatus.OK, ContentServiceStatus.CONTENT_MODIFY_SUCCESS, "컨텐츠 수정 성공"),
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
