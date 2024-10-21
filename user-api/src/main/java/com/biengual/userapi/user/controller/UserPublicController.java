package com.biengual.userapi.user.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class UserPublicController {

	// 개발용 컨트롤러
	@GetMapping("/login")
	public String login() {
		return "login";
	}
}
