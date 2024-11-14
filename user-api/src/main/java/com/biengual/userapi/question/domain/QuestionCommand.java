package com.biengual.userapi.question.domain;

public class QuestionCommand {
	public record Verify(
		String questionId,
		String answer,
		Long userId
	) {
	}

	public record GetHint(
		String questionId,
		Long userId
	) {
	}


	public record GetQuestion(
		Long contentId,
		Long userId
	) {
	}
}
