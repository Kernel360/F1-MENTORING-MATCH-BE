package com.biengual.userapi.token.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.biengual.core.domain.entity.jwt.RefreshToken;
import com.biengual.core.domain.entity.user.UserEntity;
import com.biengual.userapi.token.repository.RefreshTokenRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {
	private final RefreshTokenRepository refreshTokenRepository;

	@Transactional
	public void saveRefreshToken(UserEntity user, String newRefreshToken) {
		RefreshToken refreshToken = refreshTokenRepository.findByUserId(user.getId())
			.orElseGet(() -> {
				RefreshToken initialRefreshToken = RefreshToken.of(user.getId(), newRefreshToken);

				return refreshTokenRepository.save(initialRefreshToken);
			});

		refreshToken.updateRefreshToken(newRefreshToken);
	}
}
