package com.biengual.userapi.question.domain;

public interface QuestionStore {
	void createQuestion(Long contentId);

    // TODO: 일회용으로 쓰고 삭제 예정
    void deleteQuestions();
}
