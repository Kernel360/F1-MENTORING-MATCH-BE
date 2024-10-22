package com.biengual.userapi.user.domain;

import com.biengual.userapi.user.domain.entity.UserEntity;

public interface UserReader {
    UserEntity findUser(Long userId, String email);
}
