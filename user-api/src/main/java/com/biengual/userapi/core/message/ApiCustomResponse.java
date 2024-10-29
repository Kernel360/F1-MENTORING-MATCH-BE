package com.biengual.userapi.core.message;

import com.fasterxml.jackson.annotation.JsonInclude;

public record ApiCustomResponse(
	String code,
	String message,
	@JsonInclude(JsonInclude.Include.NON_NULL)
	Object data
) {
	public static ApiCustomResponse of(
		StatusCode statusCode, Object data
	) {
		return new ApiCustomResponse(
			statusCode.getCode(),
			statusCode.getMessage(),
			data
		);
	}

	public static ApiCustomResponse of(StatusCode statusCode) {
		return new ApiCustomResponse(
			statusCode.getCode(),
			statusCode.getMessage(),
			null
		);
	}
}
