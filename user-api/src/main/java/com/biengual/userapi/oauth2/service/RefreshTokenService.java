package com.biengual.userapi.oauth2.service;

import com.biengual.userapi.oauth2.domain.entity.RefreshToken;
import com.biengual.userapi.oauth2.repository.RefreshTokenRepository;
import com.biengual.userapi.user.domain.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
