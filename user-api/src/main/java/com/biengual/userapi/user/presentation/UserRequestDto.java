package com.biengual.userapi.user.presentation;

import static com.biengual.core.constant.BadRequestMessageConstant.*;
import static com.biengual.core.constant.RestrictionConstant.*;

import java.time.LocalDate;
import java.util.List;

import com.biengual.core.enums.Gender;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class UserRequestDto {

    // 본인 정보 수정
    public record UpdateMyInfoReq(
        String username,
        @Size(min = MIN_NICKNAME_SIZE, max = MAX_NICKNAME_SIZE, message = NICKNAME_SIZE_LIMIT_ERROR_MESSAGE)
        @Pattern(regexp = "^[a-zA-Z가-힣0-9]+( [a-zA-Z가-힣0-9]+)*$", message = NICKNAME_PATTERN_ERROR_MESSAGE)
        String nickname,
        String phoneNumber,
        LocalDate birth,
        Gender gender,
        @Size(max = MAX_CATEGORY_SELECTION_LIMIT, message = MAX_CATEGORY_SELECTION_ERROR_MESSAGE)
        List<Long> categories
    ) {
    }

}
