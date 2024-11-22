package com.biengual.userapi.validator;

import static com.biengual.core.response.error.code.UserErrorCode.*;

import java.util.regex.Pattern;

import com.biengual.core.annotation.Validator;
import com.biengual.core.response.error.exception.CommonException;

/**
 * User 도메인의 검증을 위한 클래스
 *
 * @author 김영래
 */
@Validator
public class UserValidator {

    public String verifyNicknamePattern(String nickname) {
        // 길이 검증
        if (nickname.length() < 4 || nickname.length() > 12) {
            throw new CommonException(USER_UPDATE_INFO_DENIED);
        }

        // 허용 패턴 검증 : 영어, 한글, 숫자, 단어 사이 공백 (공백은 단일 공백만 허용)
        if (!Pattern.compile("^[a-zA-Z가-힣0-9]+( [a-zA-Z가-힣0-9]+)*$").matcher(nickname).matches()) {
            throw new CommonException(USER_UPDATE_INFO_DENIED);
        }

        return nickname;
    }
}
