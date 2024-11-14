package com.biengual.core.response.error.code;

import org.springframework.http.HttpStatus;

import com.biengual.core.response.status.QuestionServiceStatus;
import com.biengual.core.response.status.ServiceStatus;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum QuestionErrorCode implements ErrorCode {
	QUESTION_NOT_FOUND(HttpStatus.NOT_FOUND, QuestionServiceStatus.QUESTION_NOT_FOUND, "문제 조회 실패"),
	QUESTION_GENERATE_API_ERROR(HttpStatus.NOT_ACCEPTABLE, QuestionServiceStatus.QUESTION_GENERATE_API_ERROR, "문제 생성 API 에러"),
	QUESTION_JSON_PARSING_ERROR(HttpStatus.CONFLICT, QuestionServiceStatus.QUESTION_JSON_PARSING_ERROR, "문제 생성 JSON 파싱 에러"),
	QUESTION_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, QuestionServiceStatus.QUESTION_ALREADY_EXISTS, "이미 퀴즈가 생성된 컨텐츠"),
	QUESTION_ALL_CORRECTED(HttpStatus.NOT_FOUND, QuestionServiceStatus.QUESTION_ALL_CORRECTED, "해당 컨텐츠의 문제 모두 정답")
	;

	private final HttpStatus httpStatus;
	private final ServiceStatus serviceStatus;
	private final String message;

	@Override
	public HttpStatus getStatus() {
		return httpStatus;
	}

	@Override
	public String getCode() {
		return serviceStatus.getServiceStatus();
	}

	@Override
	public String getMessage() {
		return message;
	}
}
