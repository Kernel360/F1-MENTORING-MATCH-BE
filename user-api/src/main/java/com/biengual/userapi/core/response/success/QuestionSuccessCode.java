package com.biengual.userapi.core.response.success;

import org.springframework.http.HttpStatus;

import com.biengual.userapi.core.response.status.QuestionServiceStatus;
import com.biengual.userapi.core.response.status.ServiceStatus;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum QuestionSuccessCode implements SuccessCode {
	QUESTION_CREATE_SUCCESS(HttpStatus.CREATED, QuestionServiceStatus.QUESTION_CREATE_SUCCESS, "문제 생성 성공"),
	QUESTION_VIEW_SUCCESS(HttpStatus.OK, QuestionServiceStatus.QUESTION_VIEW_SUCCESS, "문제 조회 성공"),

	;

	private final HttpStatus code;
	private final ServiceStatus serviceStatus;
	private final String message;

	@Override
	public HttpStatus getStatus() {
		return code;
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
