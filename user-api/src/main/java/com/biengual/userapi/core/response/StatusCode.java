package com.biengual.userapi.core.response;

import org.springframework.http.HttpStatus;

public interface StatusCode {
	HttpStatus getStatus();
	String getCode();
	String getMessage();
}
