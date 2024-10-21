package com.biengual.userapi.message.error.code;

import org.springframework.http.HttpStatus;

import com.biengual.userapi.message.status.BookmarkServiceStatus;
import com.biengual.userapi.message.status.ServiceStatus;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum BookmarkErrorCode implements ErrorCode {
	BOOKMARK_NOT_FOUND(
		HttpStatus.NOT_FOUND, BookmarkServiceStatus.BOOKMARK_NOT_FOUND, "등록된 북마크가 없습니다."
	),
	BOOKMARK_DESCRIPTION_SAME(
		HttpStatus.BAD_REQUEST, BookmarkServiceStatus.BOOKMARK_DESCRIPTION_SAME, "북마크 설명이 기존과 동일합니다."
	),
	BOOKMARK_ALREADY_EXISTS(
		HttpStatus.BAD_REQUEST, BookmarkServiceStatus.BOOKMARK_ALREADY_EXISTS, "이미 존재하는 북마크입니다."
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
