package com.biengual.userapi.message;

import com.fasterxml.jackson.annotation.JsonInclude;

public record ApiCustomResponse<T>(
	String code,
	String message,
	@JsonInclude(JsonInclude.Include.NON_NULL)
	T data // T -> Object 하고 선언문만 고쳐도 동작함
) {
	public static <T> ApiCustomResponse<T> of(
		StatusCode statusCode, T data
	) {
		return new ApiCustomResponse<>(
			statusCode.getCode(),
			statusCode.getMessage(),
			data
		);
	}

	public static <T> ApiCustomResponse<T> of(StatusCode statusCode) {
		return new ApiCustomResponse<>(
			statusCode.getCode(),
			statusCode.getMessage(),
			null
		);
	}
}
