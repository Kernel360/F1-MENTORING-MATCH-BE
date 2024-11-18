package com.biengual.userapi.question.domain;

public interface QuestionService {
	void createQuestion(Long contentId);

	QuestionInfo.DetailInfo getQuestions(QuestionCommand.GetQuestion command);

	QuestionInfo.DetailInfo getCorrectedQuestions(QuestionCommand.GetQuestion command);

	boolean verifyAnswer(QuestionCommand.Verify command);

	QuestionInfo.Hint getHint(QuestionCommand.GetHint command);
}
