package com.biengual.userapi.user.application;

import com.biengual.userapi.user.domain.enums.Gender;

import java.time.LocalDate;

public class UserCommand {

    public record UpdateMyInfo(
        String username,
        String nickname,
        String phoneNumber,
        LocalDate birth,
        Gender gender,
        String email
    ) {
    }
}
