package com.biengual.userapi.core.message.error.exception;

import com.biengual.userapi.core.message.error.code.ErrorCode;

import lombok.Getter;

@Getter
public class CommonException extends RuntimeException {
	private final transient ErrorCode errorCode;

	public CommonException(ErrorCode errorCode) {
		super(errorCode.getMessage());
		this.errorCode = errorCode;
	}
}
