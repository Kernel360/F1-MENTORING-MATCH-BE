package com.biengual.userapi.user.infrastructure;

import com.biengual.core.annotation.DataProvider;
import com.biengual.userapi.token.repository.RefreshTokenRepository;
import com.biengual.userapi.user.domain.RefreshTokenStore;

import lombok.RequiredArgsConstructor;

@DataProvider
@RequiredArgsConstructor
public class RefreshTokenStoreImpl implements RefreshTokenStore {
    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    public void delete(Long userId) {
        refreshTokenRepository.deleteByUserId(userId);
    }
}
