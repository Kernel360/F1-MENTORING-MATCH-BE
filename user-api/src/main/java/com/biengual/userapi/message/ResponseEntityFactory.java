package com.biengual.userapi.message;

import org.springframework.http.ResponseEntity;

public class ResponseEntityFactory {
	// No return Data
	public static ResponseEntity<ApiCustomResponse<Void>> toResponseEntity(StatusCode statusCode) {
		return ResponseEntity.status(statusCode.getStatus())
			.body(ApiCustomResponse.of(statusCode));
	}

	// Return Data : T type
	public static <T> ResponseEntity<ApiCustomResponse<T>> toResponseEntity(StatusCode statusCode, T data) {
		return ResponseEntity.status(statusCode.getStatus())
			.body(ApiCustomResponse.of(statusCode, data));
	}




}
