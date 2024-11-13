package com.biengual.userapi.question.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.biengual.userapi.question.domain.QuestionCommand;
import com.biengual.userapi.question.domain.QuestionInfo;
import com.biengual.userapi.question.domain.QuestionReader;
import com.biengual.userapi.question.domain.QuestionService;
import com.biengual.userapi.question.domain.QuestionStore;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class QuestionServiceImpl implements QuestionService {
    private final QuestionReader questionReader;
    private final QuestionStore questionStore;

    @Override
    @Transactional
    public void createQuestion(Long contentId) {
        questionStore.createQuestion(contentId);
    }

    @Override
    @Transactional(readOnly = true)
    public QuestionInfo.DetailInfo getQuestions(Long contentId) {
        return QuestionInfo.DetailInfo.of(questionReader.getQuestions(contentId));
    }

    @Override
    @Transactional(readOnly = true)
    public boolean verifyAnswer(QuestionCommand.Verify command) {
        return questionReader.verifyAnswer(command);
    }

    // TODO: 일회용으로 쓰고 삭제 예정
    @Transactional
    public void deleteQuestions() {
        questionStore.deleteQuestions();
    }
}