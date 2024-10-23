package com.biengual.userapi.user.presentation;

import java.time.LocalDate;
import java.util.List;

import com.biengual.userapi.user.domain.enums.Gender;

public class UserRequestDto {

	// 본인 정보 수정
	public record UpdateMyInfoReq(
		String username,
		String nickname,
		String phoneNumber,
		LocalDate birth,
		Gender gender,
		List<Long> categories
	) {

	}

}
