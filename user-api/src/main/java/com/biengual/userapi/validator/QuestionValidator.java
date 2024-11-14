package com.biengual.userapi.validator;

import static com.biengual.core.response.error.code.QuestionErrorCode.*;

import java.util.Objects;

import com.biengual.core.annotation.Validator;
import com.biengual.core.domain.document.question.QuestionDocument;
import com.biengual.core.enums.QuestionType;
import com.biengual.core.response.error.exception.CommonException;
import com.biengual.userapi.questionhistory.domain.QuestionHistoryReader;

import lombok.RequiredArgsConstructor;

@Validator
@RequiredArgsConstructor
public class QuestionValidator {
    private final QuestionHistoryReader questionHistoryReader;

    // 이미 문제를 만든 컨텐츠인지 검증
    public void verifyQuestionAlreadyGenerated(Integer numOfQuiz) {
        if (numOfQuiz > 0) {
            throw new CommonException(QUESTION_ALREADY_EXISTS);
        }
    }

    // 문제 정답 검증
    public boolean verifyAnswerOfQuestion(String answer, QuestionDocument question) {
        String parsedAnswer = (question.getType() == QuestionType.ORDER)
            ? this.parseAnswerOfOrder(answer) : answer;

        return Objects.equals(question.getAnswer(), parsedAnswer);
    }
    // 이미 맞춘 문제인지 검증

    public boolean verifyAlreadyCorrectedQuestion(Long userId, String questionId){
        return questionHistoryReader.existsCorrectedQuestionHistory(userId, questionId);
    }

    // Internal Methods ------------------------------------------------------------------------------------------------
    private String parseAnswerOfOrder(String answer) {
        String[] splitAnswer = answer.split(" ");
        StringBuilder formattedAnswer = new StringBuilder("[");
        for (int i = 0; i < splitAnswer.length; i++) {
            formattedAnswer.append(splitAnswer[i]);
            if (i < splitAnswer.length - 1) {
                formattedAnswer.append(", ");
            }
        }
        return formattedAnswer.append("]").toString();
    }
}
