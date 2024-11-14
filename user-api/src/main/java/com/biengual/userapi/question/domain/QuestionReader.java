package com.biengual.userapi.question.domain;

import java.util.List;

import com.biengual.core.domain.document.question.QuestionDocument;

public interface QuestionReader {
	List<QuestionInfo.Detail> findQuestionsByContentId(Long contentId, Long userId);

	List<QuestionInfo.Detail> findCorrectedQuestionsByContentId(Long contentId, Long userId);

	QuestionDocument findQuestionByQuestionId(String questionId);

	QuestionInfo.Hint findHintOfQuestion(String questionId);
}
