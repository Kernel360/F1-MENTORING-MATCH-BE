package com.biengual.userapi.user.service;

import static com.biengual.userapi.message.error.code.UserErrorCode.*;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.biengual.userapi.message.error.exception.CommonException;
import com.biengual.userapi.oauth2.domain.info.OAuth2UserPrincipal;
import com.biengual.userapi.oauth2.repository.RefreshTokenRepository;
import com.biengual.userapi.user.domain.dto.UserRequestDto;
import com.biengual.userapi.user.domain.dto.UserResponseDto;
import com.biengual.userapi.user.domain.entity.UserEntity;
import com.biengual.userapi.user.domain.enums.UserStatus;
import com.biengual.userapi.user.repository.UserRepository;
import com.biengual.userapi.util.CookieUtil;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class UserService {
	private final CookieUtil cookieUtil;
	private final UserRepository userRepository;
	private final RefreshTokenRepository refreshTokenRepository;

	@Transactional
	public UserResponseDto.UserUpdateResponse updateUserInfo(
		UserRequestDto.Update request, String email
	) {
		UserEntity user = this.getUserByEmail(email);
		AssertThat_UserAccountIsAppropriate(user);

		// set additional info when user created
		if (user.getUserStatus() == UserStatus.USER_STATUS_CREATED) {
			user.setUserInitialInfo(request);
		}
		// update user info
		user.updateUserInfo(request);

		return UserResponseDto.UserUpdateResponse.of(user);
	}

	@Transactional(readOnly = true)
	public UserResponseDto.UserMyPageResponse getMyPage(String email) {
		UserEntity user = this.getUserByEmail(email);
		AssertThat_UserAccountIsAppropriate(user);

		return UserResponseDto.UserMyPageResponse.of(user);
	}

	@Transactional(readOnly = true)
	public UserResponseDto.UserMyTimeResponse getMySignUpTime(Long id) {
		UserEntity user = this.getUserById(id);

		return UserResponseDto.UserMyTimeResponse.of(user);
	}

	@Transactional(readOnly = true)
	public UserResponseDto.UserChallengeResponse getMyChallenge(String email) {
		UserEntity user = this.getUserByEmail(email);
		AssertThat_UserAccountIsAppropriate(user);
		return UserResponseDto.UserChallengeResponse.toDto(user);
	}

	@Transactional(readOnly = true)
	public UserEntity getUserById(Long userId) {
		UserEntity user = userRepository.findById(userId)
			.orElseThrow(() -> new CommonException(USER_NOT_FOUND));
		AssertThat_UserAccountIsAppropriate(user);
		return user;
	}

	@Transactional
	public UserEntity getUserByOAuthUser(OAuth2UserPrincipal oAuthUser) {
		UserEntity user = userRepository.findByEmail(oAuthUser.getEmail())
			.orElseGet(() -> {
				UserEntity newUser = UserEntity.createByOAuthUser(oAuthUser);

				return userRepository.save(newUser);
			});

		AssertThat_UserAccountIsAppropriate(user);

		user.updateAfterOAuth2Login(oAuthUser);

		return user;
	}

	@Transactional
	public void logout(HttpServletRequest request, HttpServletResponse response, Long userId) {
		cookieUtil.removeAccessTokenCookie(request, response);

		cookieUtil.removeRefreshTokenCookie(request, response);

		refreshTokenRepository.deleteByUserId(userId);
	}

	public Boolean getUserStatus(HttpServletRequest request) {
		return cookieUtil.verifyAccessTokenCookie(request.getCookies());
	}

	// Internal Methods=================================================================================================

	public UserEntity getUserByEmail(String email) {
		return userRepository.findByEmail(email)
			.orElseThrow(() -> new CommonException(USER_NOT_FOUND));
	}

	protected void AssertThat_UserAccountIsAppropriate(UserEntity user) {
		if (user.getUserStatus().equals(UserStatus.USER_STATUS_DEACTIVATED)) {
			throw new CommonException(USER_FAIL_DEACTIVATE);
		}
		if (user.getUserStatus().equals(UserStatus.USER_STATUS_SUSPENDED)) {
			throw new CommonException(USER_FAIL_SUSPEND);
		}
	}
}
