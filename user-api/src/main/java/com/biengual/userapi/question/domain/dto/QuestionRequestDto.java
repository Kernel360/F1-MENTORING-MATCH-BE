package com.biengual.userapi.question.domain.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class QuestionRequestDto {
	public record QuestionCreateRequestDto(
		Integer questionNumOfBlank,
		Integer questionNumOfOrder
	) {
	}
}