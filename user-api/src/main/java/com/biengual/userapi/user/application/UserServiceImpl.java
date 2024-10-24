package com.biengual.userapi.user.application;

import com.biengual.userapi.message.error.exception.CommonException;
import com.biengual.userapi.oauth2.domain.info.OAuth2UserPrincipal;
import com.biengual.userapi.user.domain.*;
import com.biengual.userapi.user.repository.UserRepository;
import com.biengual.userapi.util.CookieUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.biengual.userapi.message.error.code.UserErrorCode.USER_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
	private final CookieUtil cookieUtil;
	private final UserRepository userRepository;
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

	@Override
	@Transactional(readOnly = true)
	public UserEntity getUserById(Long userId) {
		UserEntity user = userRepository.findById(userId)
			.orElseThrow(() -> new CommonException(USER_NOT_FOUND));

		return user;
	}

	@Override
	@Transactional
	public UserEntity getUserByOAuthUser(OAuth2UserPrincipal oAuthUser) {
		UserEntity user = userRepository.findByEmail(oAuthUser.getEmail())
			.orElseGet(() -> {
				UserEntity newUser = UserEntity.createByOAuthUser(oAuthUser);

				return userRepository.save(newUser);
			});

		user.updateAfterOAuth2Login(oAuthUser);

		return user;
	}

	@Override
	@Transactional
	public void logout(HttpServletRequest request, HttpServletResponse response, Long userId) {
		cookieUtil.removeAccessTokenCookie(request, response);

		cookieUtil.removeRefreshTokenCookie(request, response);

		refreshTokenStore.delete(userId);
	}

	@Override
	public Boolean getLoginStatus(OAuth2UserPrincipal principal) {
		return principal != null;
	}
}
