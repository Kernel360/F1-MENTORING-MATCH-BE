package com.biengual.userapi.question.domain;

public class QuestionCommand {
	public record Verify(
		String questionId,
		String answer
	) {
	}
}
