package com.biengual.userapi.user.domain;

public interface UserReader {
    UserEntity findUser(Long userId, String email);
}
