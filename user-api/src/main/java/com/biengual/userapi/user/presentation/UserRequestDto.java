package com.biengual.userapi.user.presentation;

import java.time.LocalDate;

import com.biengual.userapi.user.domain.enums.Gender;

public class UserRequestDto {

	public record UpdateMyInfoReq(
		String username,
		String nickname,
		String phoneNumber,
		LocalDate birth,
		Gender gender
	) {

	}

}
