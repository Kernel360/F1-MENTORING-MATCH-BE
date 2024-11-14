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

	public QuestionInfo.DetailInfo getQuestions(Long contentId) {
		return questionService.getQuestions(contentId);
	}

	public boolean verifyAnswer(QuestionCommand.Verify command) {
		return questionService.verifyAnswer(command);
	}

	public QuestionInfo.Hint getHint(QuestionCommand.ViewHint command) {
		return questionService.getHint(command);
	}
}
