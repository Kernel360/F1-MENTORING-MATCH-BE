package com.biengual.userapi.user.presentation;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class UserApiController {

	// 개발용 컨트롤러
	@GetMapping("/login")
	public String login() {
		return "login";
	}
}
