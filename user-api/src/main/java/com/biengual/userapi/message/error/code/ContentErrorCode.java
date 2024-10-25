package com.biengual.userapi.message.error.code;

import org.springframework.http.HttpStatus;

import com.biengual.userapi.message.status.ContentServiceStatus;
import com.biengual.userapi.message.status.ServiceStatus;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum ContentErrorCode implements ErrorCode {
	CONTENT_NOT_FOUND(HttpStatus.NOT_FOUND, ContentServiceStatus.CONTENT_NOT_FOUND, "컨텐츠 조회 실패"),

	CONTENT_SORT_COL_NOT_FOUND(
		HttpStatus.NOT_FOUND, ContentServiceStatus.CONTENT_SORT_COL_NOT_FOUND, "컨텐츠 정렬 조건 입력 오류"
	),
	CONTENT_SEARCH_WORD_NOT_FOUND(
		HttpStatus.NOT_FOUND, ContentServiceStatus.CONTENT_SEARCH_WORD_NOT_FOUND, "컨텐츠 검색어 입력 오류"
	),
	CONTENT_TYPE_NOT_FOUND(
		HttpStatus.NOT_FOUND, ContentServiceStatus.CONTENT_TYPE_NOT_FOUND, "컨텐츠 타입 오류"
	)
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
