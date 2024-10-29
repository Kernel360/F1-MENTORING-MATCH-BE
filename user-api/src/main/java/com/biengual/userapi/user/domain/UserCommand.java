package com.biengual.userapi.user.domain;


import lombok.Builder;

import java.time.LocalDate;
import java.util.List;

import com.biengual.core.domain.entity.category.CategoryEntity;
import com.biengual.core.domain.entity.user.UserCategoryEntity;
import com.biengual.core.enums.Gender;

public class UserCommand {

    // 본인 정보 수정
    @Builder
    public record UpdateMyInfo(
        String username,
        String nickname,
        String phoneNumber,
        LocalDate birth,
        Gender gender,
        List<Long> categoryIds,
        Long userId,
        String email
    ) {
        public UserCategoryEntity toUserCategoryEntity(CategoryEntity category) {
            return UserCategoryEntity.builder()
                .userId(this.userId)
                .category(category)
                .build();
        }
    }
}
