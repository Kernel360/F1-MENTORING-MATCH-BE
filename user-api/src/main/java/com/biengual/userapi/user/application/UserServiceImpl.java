package com.biengual.userapi.user.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.biengual.core.domain.entity.user.UserEntity;
import com.biengual.core.enums.PointReason;
import com.biengual.core.util.CookieUtil;
import com.biengual.userapi.oauth2.info.OAuth2UserPrincipal;
import com.biengual.userapi.point.domain.PointManager;
import com.biengual.userapi.user.domain.RefreshTokenStore;
import com.biengual.userapi.user.domain.UserCommand;
import com.biengual.userapi.user.domain.UserInfo;
import com.biengual.userapi.user.domain.UserReader;
import com.biengual.userapi.user.domain.UserService;
import com.biengual.userapi.user.domain.UserStore;
import com.biengual.userapi.validator.PointValidator;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final CookieUtil cookieUtil;
    private final UserReader userReader;
    private final UserStore userStore;
    private final RefreshTokenStore refreshTokenStore;
    private final PointValidator pointValidator;
    private final PointManager pointManager;

    // 본인 정보 조회
    @Override
    @Transactional(readOnly = true)
    public UserInfo.MyInfo getMyInfo(Long userId) {
        return userReader.findMyInfo(userId);
    }

    // 본인 정보 수정
    @Override
    @Transactional
    public void updateMyInfo(UserCommand.UpdateMyInfo command) {
        userStore.updateMyInfo(command);
    }

    // 본인 회원 가입 날짜 조회
    @Override
    @Transactional(readOnly = true)
    public UserInfo.MySignUpTime getMySignUpTime(Long userId) {
        return userReader.findMySignUpTime(userId);
    }

    // OAuth 유저 회원가입, 로그인 처리 및 미션 생성
    @Override
    @Transactional
    public UserEntity getUserByOAuthUser(OAuth2UserPrincipal principal) {
        UserEntity user = userReader.findUser(principal);

        // 유저 매일 첫 로그인 포인트 업데이트
        if (pointValidator.verifyFirstDailyLogin(user)) {
            pointManager.updateAndSavePoint(PointReason.FIRST_DAILY_LOG_IN, user.getId());
        }

        // 유저 마지막 로그인 시간 업데이트
        userStore.updateLastLoginTime(user.getId());

        return user;
    }

    // 유저 로그아웃
    @Override
    @Transactional
    public void logout(HttpServletRequest request, HttpServletResponse response, Long userId) {
        cookieUtil.removeAccessTokenCookie(request, response);
        cookieUtil.removeRefreshTokenCookie(request, response);

        refreshTokenStore.delete(userId);
    }

    // 유저 로그인 상태 확인
    @Override
    public Boolean getLoginStatus(OAuth2UserPrincipal principal) {
        return principal != null;
    }
}
