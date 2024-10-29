package com.biengual.userapi.user.presentation;

import java.time.LocalDate;
import java.util.List;

import com.biengual.userapi.core.common.enums.Gender;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import static com.biengual.userapi.core.constant.BadRequestMessageConstant.MAX_CATEGORY_SELECTION_ERROR_MESSAGE;
import static com.biengual.userapi.core.constant.BadRequestMessageConstant.NULL_CATEGORY_LIST_ERROR_MESSAGE;
import static com.biengual.userapi.core.constant.RestrictionConstant.MAX_CATEGORY_SELECTION_LIMIT;

public class UserRequestDto {

	// 본인 정보 수정
	public record UpdateMyInfoReq(
		String username,
		String nickname,
		String phoneNumber,
		LocalDate birth,
		Gender gender,
		@NotNull(message = NULL_CATEGORY_LIST_ERROR_MESSAGE)
		@Size(max = MAX_CATEGORY_SELECTION_LIMIT, message = MAX_CATEGORY_SELECTION_ERROR_MESSAGE)
		List<Long> categories
	) {

	}

}
