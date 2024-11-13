package com.biengual.userapi.question.presentation;

import java.util.List;

import com.biengual.core.enums.QuestionType;

import lombok.Builder;

public class QuestionResponseDto {

	@Builder
	public record View(
		String question,
		String questionId,
		List<String> examples,
		QuestionType type
	) {
	}

	@Builder
	public record ViewListRes(
		List<View> questionAnswer
	) {
	}
}
