package com.biengual.userapi.user.presentation;

import com.biengual.userapi.user.domain.UserEntity;
import com.biengual.userapi.user.domain.enums.Gender;
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

	public record UserUpdateResponse(
		Long userId
	) {
		public static UserUpdateResponse of(UserEntity user) {
			return new UserUpdateResponse(
				user.getId()
			);
		}

	}

	public record UserMyTimeResponse(
		LocalDateTime createdAt,
		LocalDateTime updatedAt
	) {
		public static UserMyTimeResponse of(UserEntity user) {
			return new UserMyTimeResponse(
				user.getCreatedAt(),
				user.getUpdatedAt()
			);
		}

	}
}
