package com.biengual.userapi.message.error;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.biengual.userapi.message.ApiCustomResponse;
import com.biengual.userapi.message.ResponseEntityFactory;
import com.biengual.userapi.message.error.exception.CommonException;

@RestControllerAdvice
public class GlobalExceptionHandler {
	@ExceptionHandler(CommonException.class)
	public ResponseEntity<Object> commonException(CommonException e) {
		return ResponseEntityFactory.toResponseEntity(e.getErrorCode());
	}

}
