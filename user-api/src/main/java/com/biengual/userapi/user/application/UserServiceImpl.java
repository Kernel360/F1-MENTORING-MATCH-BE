package com.biengual.userapi.user.application;

import com.biengual.userapi.core.entity.user.UserEntity;
import com.biengual.userapi.core.oauth2.domain.info.OAuth2UserPrincipal;
import com.biengual.userapi.user.domain.*;
import com.biengual.userapi.core.common.util.CookieUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
	private final CookieUtil cookieUtil;
	private final UserReader userReader;
	private final UserStore userStore;
	private final RefreshTokenStore refreshTokenStore;

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

	// OAuth 유저 회원가입 및 로그인 처리
	@Override
	@Transactional
	public UserEntity getUserByOAuthUser(OAuth2UserPrincipal principal) {
		return userReader.findUser(principal);
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
