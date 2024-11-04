package com.biengual.userapi.user.application;

import com.biengual.core.annotation.Facade;
import com.biengual.core.domain.entity.user.UserEntity;
import com.biengual.userapi.oauth2.info.OAuth2UserPrincipal;
import com.biengual.userapi.point.domain.PointService;
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
    private final PointService pointService;

    // 회원가입 및 로그인
    public UserEntity getUserByOAuthUser(OAuth2UserPrincipal principal) {
        UserEntity user =  userService.getUserByOAuthUser(principal);

        // 유저 그날의 첫 로그인 포인트 업데이트
        pointService.updatePointFirstDailyLogin(user.getId());

        return user;
    }

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
