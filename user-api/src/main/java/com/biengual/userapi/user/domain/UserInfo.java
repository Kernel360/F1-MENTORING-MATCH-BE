package com.biengual.userapi.user.domain;

import com.biengual.userapi.user.domain.enums.Gender;

import java.time.LocalDate;
import java.util.List;

public class UserInfo {
    public record MyCategory(
        Long id,
        String name
    ) {
    }

    public record MyInfo(
        String username,
        String nickname,
        String email,
        String phoneNumber,
        LocalDate birth,
        Gender gender,
        List<MyCategory> myCategories
    ) {
    }
}
