package com.biengual.core.response.error.exception;

import com.biengual.core.response.error.code.ErrorCode;

import lombok.Getter;

@Getter
public class CommonException extends RuntimeException {
	private final transient ErrorCode errorCode;

	public CommonException(ErrorCode errorCode) {
		super(errorCode.getMessage());
		this.errorCode = errorCode;
	}
}
