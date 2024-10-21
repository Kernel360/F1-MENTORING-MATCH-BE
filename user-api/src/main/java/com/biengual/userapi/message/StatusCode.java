package com.biengual.userapi.message;

import org.springframework.http.HttpStatus;

public interface StatusCode {
	HttpStatus getStatus();
	String getCode();
	String getMessage();
}
