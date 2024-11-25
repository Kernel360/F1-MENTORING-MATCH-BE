package com.biengual.core.response.error.code;

import org.springframework.http.HttpStatus;

import com.biengual.core.response.status.ContentServiceStatus;
import com.biengual.core.response.status.ServiceStatus;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum ContentErrorCode implements ErrorCode {
	CONTENT_NOT_FOUND(HttpStatus.NOT_FOUND, ContentServiceStatus.CONTENT_NOT_FOUND, "컨텐츠 조회 실패"),
	CONTENT_SORT_COL_NOT_FOUND(
		HttpStatus.NOT_FOUND, ContentServiceStatus.CONTENT_SORT_COL_NOT_FOUND, "컨텐츠 정렬 조건 입력 오류"
	),
	CONTENT_TYPE_NOT_FOUND(
		HttpStatus.NOT_FOUND, ContentServiceStatus.CONTENT_TYPE_NOT_FOUND, "컨텐츠 타입 오류"
	),
	CONTENT_IS_DEACTIVATED(HttpStatus.FORBIDDEN, ContentServiceStatus.CONTENT_IS_DEACTIVATED, "비활성화 컨텐츠"),
	CONTENT_LEVEL_FEEDBACK_HISTORY_ALREADY_EXISTS(HttpStatus.BAD_REQUEST,
		ContentServiceStatus.CONTENT_LEVEL_FEEDBACK_HISTORY_ALREADY_EXISTS, "이미 제출한 해당 컨텐츠의 난이도 피드백"),
	UNPAID_RECENT_CONTENT(HttpStatus.FORBIDDEN, ContentServiceStatus.UNPAID_RECENT_CONTENT,
		"포인트를 지불하지 않은 최신 컨텐츠"),
	CONTENT_LEVEL_FEEDBACK_DATA_MART_NOT_FOUND(HttpStatus.NOT_FOUND,
		ContentServiceStatus.CONTENT_LEVEL_FEEDBACK_DATA_MART_NOT_FOUND, "컨텐츠 레벨 피드백 데이터 마트 조회 실패"),
	CONTENT_NEED_LOGIN(HttpStatus.UNAUTHORIZED, ContentServiceStatus.CONTENT_NEED_LOGIN, "로그인이 필요한 컨텐츠");

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
