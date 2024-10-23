package com.biengual.userapi.user.presentation;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.biengual.userapi.user.domain.UserEntity;
import com.biengual.userapi.user.domain.enums.Gender;

public class UserResponseDto {
	public record UserUpdateResponse(
		Long userId
	) {
		public static UserUpdateResponse of(UserEntity user) {
			return new UserUpdateResponse(
				user.getId()
			);
		}

	}

	public record UserMyPageResponse(
		String username,
		String nickname,
		String email,
		String phoneNumber,
		LocalDate birth,
		Gender gender
	) {
		public static UserMyPageResponse of(UserEntity user) {
			return new UserMyPageResponse(
				user.getUsername(),
				user.getNickname(),
				user.getEmail(),
				user.getPhoneNumber(),
				user.getBirth(),
				user.getGender()
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

	public record UserChallengeResponse(
		// TODO: challenge 완성되면 수정 필요, 아마 querydsl 사용해야 할 수도
		String challenge
	) {
		public static UserChallengeResponse toDto(UserEntity user) {
			return new UserChallengeResponse("user's challenge");
		}
	}
}
