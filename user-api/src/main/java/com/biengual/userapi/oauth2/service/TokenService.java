package com.biengual.userapi.oauth2.service;

import com.biengual.userapi.oauth2.TokenProvider;
import com.biengual.userapi.user.domain.UserService;
import com.biengual.userapi.user.domain.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TokenService {
	private final TokenProvider tokenProvider;
	private final RefreshTokenService refreshTokenService;
	private final UserService userService;

	public String createNewAccessToken(String refreshToken) {
		tokenProvider.validateToken(refreshToken);

		Long userId = refreshTokenService.getRefreshToken(refreshToken).getUserId();
		UserEntity user = userService.getUserById(userId);

		return tokenProvider.generateAccessToken(user);
	}
}
