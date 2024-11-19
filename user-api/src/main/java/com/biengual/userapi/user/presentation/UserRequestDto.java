package com.biengual.userapi.user.presentation;

import static com.biengual.core.constant.BadRequestMessageConstant.*;
import static com.biengual.core.constant.RestrictionConstant.*;

import java.time.LocalDate;
import java.util.List;

import com.biengual.core.enums.Gender;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;


public class UserRequestDto {

	// 본인 정보 수정
	public record UpdateMyInfoReq(
		String username,
		String nickname,
		String phoneNumber,
		LocalDate birth,
		Gender gender,
		@Size(max = MAX_CATEGORY_SELECTION_LIMIT, message = MAX_CATEGORY_SELECTION_ERROR_MESSAGE)
		List<Long> categories
	) {

	}

}
