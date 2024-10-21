package com.biengual.userapi.controller;

import com.biengual.userapi.user.controller.UserApiController;
import com.biengual.userapi.user.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(UserApiController.class)
class UserApiControllerTest {

	@Autowired
	MockMvc mockMvc;

	@MockBean
	UserService userService;
	
	@Test
	@DisplayName("신규 유저 등록 테스트")
	void set_new_user_info_test() {

	}

	@Test
	@DisplayName("유저가 본인의 정보를 조회하는 테스트")
	void get_my_page_test() {
		
	}

	@Test
	@DisplayName("유저 정보 수정 테스트")
	void update_existed_user_info_test() {
		
	}

	@Test
	@DisplayName("유저가 본인의 챌린지 조회하는 테스트")
	void user_challenge_test() {

	}
}