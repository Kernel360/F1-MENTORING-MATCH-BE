package com.biengual.userapi.user.domain;

import com.biengual.userapi.user.domain.enums.Gender;
import lombok.Builder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class UserInfo {
    public record MyCategory(
        Long id,
        String name
    ) {
    }

    @Builder
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

    public record MyInfoExceptMyCategories(
        String username,
        String nickname,
        String email,
        String phoneNumber,
        LocalDate birth,
        Gender gender
    ) {
    }

    public record MySignUpTime(
        LocalDateTime createdAt,
        LocalDateTime updatedAt
    ) {
    }
}
