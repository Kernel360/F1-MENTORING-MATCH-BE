package com.biengual.userapi.user.domain;

import com.biengual.userapi.core.entity.category.CategoryEntity;
import com.biengual.userapi.core.common.enums.Gender;
import com.biengual.userapi.core.entity.user.UserCategoryEntity;

import lombok.Builder;

import java.time.LocalDate;
import java.util.List;

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
