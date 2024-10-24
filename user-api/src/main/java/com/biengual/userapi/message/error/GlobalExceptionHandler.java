package com.biengual.userapi.message.error;

import com.biengual.userapi.message.ResponseEntityFactory;
import com.biengual.userapi.message.error.code.RequestErrorCode;
import com.biengual.userapi.message.error.exception.CommonException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
	// 공통적인 Exception 처리
	@ExceptionHandler(CommonException.class)
	public ResponseEntity<Object> commonException(CommonException e) {
		return ResponseEntityFactory.toResponseEntity(e.getErrorCode());
	}

	// Request 검증 Exception 처리
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Object> RequestException(MethodArgumentNotValidException e) {
		RequestErrorCode errorCode = RequestErrorCode.BAD_REQUEST;
		errorCode.setMessage(e);
		log.error(errorCode.getMessage());
		return ResponseEntityFactory.toResponseEntity(errorCode);
	}

	// 유효하지 않은 JSON 형식 요청 Exception 처리
	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException e) {
		RequestErrorCode errorCode = RequestErrorCode.BAD_REQUEST;
		errorCode.setMessage(e);
		log.error(e.getMessage());
		return ResponseEntityFactory.toResponseEntity(errorCode);
	}
}
