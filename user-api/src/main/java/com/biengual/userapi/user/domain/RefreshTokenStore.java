package com.biengual.userapi.user.domain;

public interface RefreshTokenStore {
    void delete(Long userId);
}
