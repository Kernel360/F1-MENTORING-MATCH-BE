package com.biengual.userapi.user.domain.dto;

import java.time.LocalDate;

import com.biengual.userapi.user.domain.enums.Gender;

public class UserRequestDto {

	public record Update(
		String username,
		String nickname,
		String phoneNumber,
		LocalDate birth,
		Gender gender
	) {

	}

}
