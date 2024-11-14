package com.biengual.userapi.question.domain;

public interface QuestionService {
	void createQuestion(Long contentId);

	QuestionInfo.DetailInfo getQuestions(Long contentId);

	boolean verifyAnswer(QuestionCommand.Verify command);

	QuestionInfo.Hint getHint(QuestionCommand.ViewHint command);
}
