package com.biengual.userapi.user.application;

import com.biengual.userapi.message.error.exception.CommonException;
import com.biengual.userapi.oauth2.domain.info.OAuth2UserPrincipal;
import com.biengual.userapi.oauth2.repository.RefreshTokenRepository;
import com.biengual.userapi.user.domain.UserCommand;
import com.biengual.userapi.user.domain.UserService;
import com.biengual.userapi.user.domain.UserStore;
import com.biengual.userapi.user.domain.UserEntity;
import com.biengual.userapi.user.presentation.UserResponseDto;
import com.biengual.userapi.user.repository.UserRepository;
import com.biengual.userapi.util.CookieUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.biengual.userapi.message.error.code.UserErrorCode.*;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
	private final CookieUtil cookieUtil;
	private final UserRepository userRepository;
	private final RefreshTokenRepository refreshTokenRepository;
	private final UserStore userStore;

	// 본인 정보 수정
	@Override
	@Transactional
	public void updateMyInfo(UserCommand.UpdateMyInfo command) {
		userStore.updateMyInfo(command);
	}

	@Override
	@Transactional(readOnly = true)
	public UserResponseDto.UserMyPageResponse getMyPage(String email) {
		UserEntity user = this.getUserByEmail(email);

		return UserResponseDto.UserMyPageResponse.of(user);
	}

	@Override
	@Transactional(readOnly = true)
	public UserResponseDto.UserMyTimeResponse getMySignUpTime(Long userId) {
		UserEntity user = this.getUserById(userId);

		return UserResponseDto.UserMyTimeResponse.of(user);
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

		refreshTokenRepository.deleteByUserId(userId);
	}

	@Override
	public Boolean getUserStatus(HttpServletRequest request) {
		return cookieUtil.verifyAccessTokenCookie(request.getCookies());
	}

	// Internal Methods=================================================================================================

	public UserEntity getUserByEmail(String email) {
		return userRepository.findByEmail(email)
			.orElseThrow(() -> new CommonException(USER_NOT_FOUND));
	}
}
