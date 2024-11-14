package com.biengual.core.response.status;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum QuestionServiceStatus implements ServiceStatus {
	// success
	QUESTION_CREATE_SUCCESS("U-Q-001"),
	QUESTION_VIEW_SUCCESS("U-Q-002"),
	QUESTION_ANSWER_VIEW_SUCCESS("U-Q-003"),

	// error
	QUESTION_NOT_FOUND("U-Q-901"),
	QUESTION_GENERATE_API_ERROR("U-Q-902"),
	QUESTION_JSON_PARSING_ERROR("U-Q-903"),
	QUESTION_ALREADY_EXISTS("U-Q-904"),
	QUESTION_ALL_CORRECTED("U-Q-905")
	;

	private final String code;

	@Override
	public String getServiceStatus() {
		return code;
	}
}
