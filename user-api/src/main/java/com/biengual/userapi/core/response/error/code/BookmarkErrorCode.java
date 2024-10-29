package com.biengual.userapi.core.response.error.code;

import org.springframework.http.HttpStatus;

import com.biengual.userapi.core.response.status.BookmarkServiceStatus;
import com.biengual.userapi.core.response.status.ServiceStatus;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum BookmarkErrorCode implements ErrorCode {
	BOOKMARK_NOT_FOUND(
		HttpStatus.NOT_FOUND, BookmarkServiceStatus.BOOKMARK_NOT_FOUND, "북마크 조회 실패"
	),
	BOOKMARK_ALREADY_EXISTS(
		HttpStatus.BAD_REQUEST, BookmarkServiceStatus.BOOKMARK_ALREADY_EXISTS, "북마크 중복"
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
