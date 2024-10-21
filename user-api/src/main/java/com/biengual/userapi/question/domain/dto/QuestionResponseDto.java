package com.biengual.userapi.question.domain.dto;

import java.util.List;

import com.biengual.userapi.question.domain.enums.QuestionType;

public class QuestionResponseDto {
	public record QuestionCreateResponseDto(
		List<String> questionDocumentIds
	) {
	}

	public record QuestionViewResponseDto (
		String question,
		String questionKo,
		String answer,
		QuestionType type
	){
	}
}
