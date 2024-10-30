package com.biengual.core.response.status;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum QuestionServiceStatus implements ServiceStatus {
	// success
	QUESTION_CREATE_SUCCESS("U-Q-001"),
	QUESTION_VIEW_SUCCESS("U-Q-002"),

	// error
	QUESTION_NOT_FOUND("U-Q-901"),

	;

	private final String code;

	@Override
	public String getServiceStatus() {
		return code;
	}
}
