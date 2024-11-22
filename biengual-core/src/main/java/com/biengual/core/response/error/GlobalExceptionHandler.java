package com.biengual.core.response.error;

import com.biengual.core.response.ResponseEntityFactory;
import com.biengual.core.response.error.code.RequestErrorCode;
import com.biengual.core.response.error.exception.CommonException;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static com.biengual.core.response.error.code.ServerError.SERVER_ERROR;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
	// 공통적인 Exception 처리
	@ExceptionHandler(CommonException.class)
	public ResponseEntity<Object> handleCommonException(CommonException e) {
		return ResponseEntityFactory.toResponseEntity(e.getErrorCode());
	}

	// RequestDto 검증 Exception 처리
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Object> handleRequestException(MethodArgumentNotValidException e) {
		RequestErrorCode errorCode = RequestErrorCode.BAD_REQUEST;
		errorCode.setMessage(e);
		return ResponseEntityFactory.toResponseEntity(errorCode);
	}

	// @RequestParam, @PathVariable 검증 Exception 처리
	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<Object> RequestParamException(ConstraintViolationException e) {
		RequestErrorCode errorCode = RequestErrorCode.BAD_REQUEST;
		errorCode.setMessage(e);
		return ResponseEntityFactory.toResponseEntity(errorCode);
	}

	// 유효하지 않은 JSON 형식 요청 Exception 처리
	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity<Object> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
		RequestErrorCode errorCode = RequestErrorCode.BAD_REQUEST;
		errorCode.setMessage(e);
		return ResponseEntityFactory.toResponseEntity(errorCode);
	}

	// 핸들링하지 않는 Exception 처리 - 실제 Exception 메시지는 로그에만 남도록 함
	@ExceptionHandler(Exception.class)
	public ResponseEntity<Object> handleOtherException(Exception e) {
		return ResponseEntityFactory.toResponseEntity(SERVER_ERROR);
	}
}
