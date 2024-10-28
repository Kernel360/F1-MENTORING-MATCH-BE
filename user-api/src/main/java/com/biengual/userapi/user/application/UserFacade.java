package com.biengual.userapi.user.application;

import com.biengual.userapi.annotation.Facade;
import com.biengual.userapi.oauth2.domain.info.OAuth2UserPrincipal;
import com.biengual.userapi.user.domain.UserCommand;
import com.biengual.userapi.user.domain.UserInfo;
import com.biengual.userapi.user.domain.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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

    // 로그아웃
    public void logout(HttpServletRequest request, HttpServletResponse response, Long userId) {
        userService.logout(request, response, userId);
    }

    // 유저 로그인 상태 확인
    public boolean getLoginStatus(OAuth2UserPrincipal principal) {
        return userService.getLoginStatus(principal);
    }
}
