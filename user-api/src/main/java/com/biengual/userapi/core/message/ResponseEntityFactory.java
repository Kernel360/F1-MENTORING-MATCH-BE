package com.biengual.userapi.core.message;

import org.springframework.http.ResponseEntity;

public class ResponseEntityFactory {
	// No return Data
	public static ResponseEntity<Object> toResponseEntity(StatusCode statusCode) {
		return ResponseEntity.status(statusCode.getStatus())
			.body(ApiCustomResponse.of(statusCode));
	}

	// Return Data : T type
	public static ResponseEntity<Object> toResponseEntity(StatusCode statusCode, Object data) {
		return ResponseEntity.status(statusCode.getStatus())
			.body(ApiCustomResponse.of(statusCode, data));
	}




}
