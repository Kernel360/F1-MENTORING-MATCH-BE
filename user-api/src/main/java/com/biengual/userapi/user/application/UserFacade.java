package com.biengual.userapi.user.application;

import com.biengual.userapi.annotation.Facade;
import com.biengual.userapi.user.domain.UserCommand;
import com.biengual.userapi.user.domain.UserInfo;
import com.biengual.userapi.user.domain.UserService;
import lombok.RequiredArgsConstructor;

@Facade
@RequiredArgsConstructor
public class UserFacade {
    private final UserService userService;

    // 본인 정보 조회
    public UserInfo.MyInfo getMyInfo(Long userId) {
        return userService.getMyInfo(userId);
    }

    // 본인 정보 수정
    public void updateMyInfo(UserCommand.UpdateMyInfo command) {
        userService.updateMyInfo(command);
    }

    // 본인 회원가입 날짜 조회
    public UserInfo.MySignUpTime getMySignUpTime(Long userId) {
        return userService.getMySignUpTime(userId);
    }
}
