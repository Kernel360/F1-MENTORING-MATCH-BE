/*
package com.biengual.userapi.user.service;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.biengual.userapi.exception.user.UserNotFoundException;
import com.biengual.userapi.user.presentation.UserRequestDto;
import com.biengual.userapi.user.presentation.UserResponseDto;
import com.biengual.userapi.core.entity.user.UserEntity;
import com.biengual.userapi.core.common.enums.Gender;
import com.biengual.userapi.core.common.enums.UserStatus;
import com.biengual.userapi.user.domain.UserRepository;

class UserServiceTest {

	@Mock
	private UserRepository userRepository;

	@InjectMocks
	private UserService userService;

	private UserEntity userEntity;

	Long id;
	String email;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
		userEntity = mock(UserEntity.class);
		id = 1L;
		email = "test@test.com";

		when(userRepository.findByEmail(email)).thenReturn(Optional.of(userEntity));
		when(userEntity.getGender()).thenReturn(Gender.GENDER_UNKNOWN);
		when(userEntity.getUserStatus()).thenReturn(UserStatus.USER_STATUS_ACTIVATE);
	}

	@Test
	@DisplayName("User 정보 업데이트 테스트")
	void update_user_info_test() {
		// Given
		UserRequestDto.UpdateMyInfo request = mock(UserRequestDto.UpdateMyInfo.class);

		// When
		when(userEntity.getUserStatus()).thenReturn(UserStatus.USER_STATUS_CREATED);
		UserResponseDto.UserUpdateResponse actualResponse = userService.updateUserInfo(request, email);

		// Then
		assertThat(actualResponse).isEqualTo(UserResponseDto.UserUpdateResponse.toDto(userEntity));
	}

	@Test
	@DisplayName("getMyPage 함수 호출 시 Response.toDto type 으로 나오는지 테스트")
	void get_my_page_test() {
		assertThat(UserResponseDto.UserMyPageResponse.toDto(userEntity)).isNotNull();
	}

	@Test
	@DisplayName("getMyChallenge 함수 호출 시 Response.toDto type 으로 나오는지 테스트")
	void get_my_challenge_test() {
		assertThat(UserResponseDto.UserChallengeResponse.toDto(userEntity)).isNotNull();
	}

	@Test
	@DisplayName("id 로 User 찾는 테스트")
	void get_user_by_id_test() {
		// When
		when(userRepository.findById(id)).thenReturn(Optional.of(userEntity));

		// Then
		assertThat(userService.getUserById(id)).isEqualTo(userEntity);
	}

	@Test
	@DisplayName("id 로 User 찾지 못했을 때 예외처리 테스트")
	void test_throws_UserNotFoundException_if_userId_does_not_exist() {
		// When
		when(userRepository.findById(id)).thenReturn(Optional.empty());

		// Then
		Assertions.assertThrows(UserNotFoundException.class, () -> {
			userService.getUserById(id);
		});
	}

	@Test
	@DisplayName("email 로 User 찾는 테스트")
	void get_user_by_email_test() {
		assertThat(userService.getUserByEmail(email)).isEqualTo(userEntity);
	}

	@Test
	@DisplayName("email 로 User 찾지 못했을 때 예외처리 테스트")
	void test_throws_UserNotFoundException_if_userEmail_does_not_exist() {
		// When
		when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

		// Then
		Assertions.assertThrows(RuntimeException.class, () -> {
			userService.getUserByEmail(email);
		});
	}

	@Test
	@DisplayName("User 의 계정 상태가 비활성화 상태인지 확인")
	void test_throws_RuntimeException_if_user_status_deactivated() {
		// When
		when(userEntity.getUserStatus()).thenReturn(UserStatus.USER_STATUS_DEACTIVATED);

		// Then
		Assertions.assertThrows(RuntimeException.class, () -> {
			userService.AssertThat_UserAccountIsAppropriate(userEntity);
		});
	}

	@Test
	@DisplayName("User 의 계정 상태가 정지 상태인지 확인")
	void test_throws_RuntimeException_if_user_status_suspended() {
		// When
		when(userEntity.getUserStatus()).thenReturn(UserStatus.USER_STATUS_SUSPENDED);

		//Then
		Assertions.assertThrows(RuntimeException.class, () -> {
			userService.AssertThat_UserAccountIsAppropriate(userEntity);
		});
	}
}*/
