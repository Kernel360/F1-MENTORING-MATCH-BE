package com.biengual.userapi.user.domain;

import com.biengual.userapi.user.domain.enums.Gender;

import java.time.LocalDate;

public class UserCommand {

    public record UpdateMyInfo(
        String username,
        String nickname,
        String phoneNumber,
        LocalDate birth,
        Gender gender,
        Long userId,
        String email
    ) {
    }
}
