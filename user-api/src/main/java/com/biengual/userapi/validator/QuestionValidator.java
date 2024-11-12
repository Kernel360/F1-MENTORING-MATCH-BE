package com.biengual.userapi.validator;

import static com.biengual.core.response.error.code.QuestionErrorCode.*;

import com.biengual.core.annotation.Validator;
import com.biengual.core.response.error.exception.CommonException;

import lombok.RequiredArgsConstructor;

@Validator
@RequiredArgsConstructor
public class QuestionValidator {
    // 이미 문제를 만든 컨텐츠인지 확인하기 위한 메서드
    public void verifyQuizAlreadyGenerated(Integer numOfQuiz) {
        if (numOfQuiz > 0) {
            throw new CommonException(QUESTION_ALREADY_EXISTS);
        }
    }

}
