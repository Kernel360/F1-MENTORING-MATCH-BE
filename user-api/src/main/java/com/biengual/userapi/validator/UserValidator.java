package com.biengual.userapi.validator;

import static com.biengual.core.response.error.code.UserErrorCode.*;

import com.biengual.core.annotation.Validator;
import com.biengual.core.response.error.exception.CommonException;

import lombok.RequiredArgsConstructor;

/**
 * User 도메인의 검증을 위한 클래스
 *
 * @author 김영래
 */
@Validator
@RequiredArgsConstructor
public class UserValidator {

    // 유저 정보 업데이트 시 닉네임 길이 검증
    public String verifyNicknameLength(String nickname){
        if(nickname.length() > 12){
            throw new CommonException(USER_UPDATE_INFO_DENIED);
        }
        return nickname;
    }
}
