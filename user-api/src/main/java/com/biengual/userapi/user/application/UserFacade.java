package com.biengual.userapi.user.application;

import com.biengual.userapi.annotation.Facade;
import com.biengual.userapi.user.domain.UserCommand;
import com.biengual.userapi.user.domain.UserService;
import lombok.RequiredArgsConstructor;

@Facade
@RequiredArgsConstructor
public class UserFacade {
    private final UserService userService;

    public void updateMyInfo(UserCommand.UpdateMyInfo command) {
        userService.updateMyInfo(command);
    }
}
