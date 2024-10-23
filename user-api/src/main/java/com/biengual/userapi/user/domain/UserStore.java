package com.biengual.userapi.user.domain;

public interface UserStore {
    void updateMyInfo(UserCommand.UpdateMyInfo command);
}
