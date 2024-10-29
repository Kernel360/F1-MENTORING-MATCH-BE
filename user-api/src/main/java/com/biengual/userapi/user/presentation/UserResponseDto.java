package com.biengual.userapi.user.presentation;

import com.biengual.userapi.core.common.enums.Gender;
import lombok.Builder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class UserResponseDto {

	@Builder
	public record MyCategory(
		Long id,
		String name
	) {
	}

	@Builder
	public record MyInfoRes(
		String username,
		String nickname,
		String email,
		String phoneNumber,
		LocalDate birth,
		Gender gender,
		List<MyCategory> myCategories
	) {
	}

	public record MySignUpTimeRes(
		LocalDateTime createdAt,
		LocalDateTime updatedAt
	) {
	}
}
