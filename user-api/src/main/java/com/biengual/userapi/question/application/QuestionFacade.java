package com.biengual.userapi.question.application;

import com.biengual.core.annotation.Facade;
import com.biengual.userapi.question.domain.QuestionCommand;
import com.biengual.userapi.question.domain.QuestionInfo;
import com.biengual.userapi.question.domain.QuestionService;

import lombok.RequiredArgsConstructor;

@Facade
@RequiredArgsConstructor
public class QuestionFacade {
	private final QuestionService questionService;

	public void createQuestion(Long contentId) {
		questionService.createQuestion(contentId);
	}

	public QuestionInfo.DetailInfo getQuestions(QuestionCommand.View command) {
		return questionService.getQuestions(command);
	}

	public boolean verifyAnswer(QuestionCommand.Verify command) {
		return questionService.verifyAnswer(command);
	}

	public QuestionInfo.Hint getHint(QuestionCommand.GetHint command) {
		return questionService.getHint(command);
	}

	public QuestionInfo.DetailInfo getCorrectQuestions(QuestionCommand.View command) {
		return questionService.getCorrectedQuestions(command);
	}
}
