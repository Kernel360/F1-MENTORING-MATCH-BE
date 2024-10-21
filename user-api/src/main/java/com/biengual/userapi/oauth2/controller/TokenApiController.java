package com.biengual.userapi.oauth2.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.biengual.userapi.oauth2.domain.dto.TokenDto;
import com.biengual.userapi.oauth2.service.TokenService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@Tag(name = "OAuth2 - Token API", description = "Authorization 관련 토큰 획득 API")
public class TokenApiController {
	private final TokenService tokenService;

	@PostMapping("/api/token")
	@Operation(summary = "토큰 확인 API", description = "Refresh 토큰으로 Access 토큰 생성")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "201", description = "Access 토큰이 성공적으로 생성되었습니다.", content = @Content(mediaType = "application/json")),
		@ApiResponse(responseCode = "400", description = "잘못된 요청입니다.", content = @Content(mediaType = "application/json")),
		@ApiResponse(responseCode = "401", description = "인증되지 않은 사용자입니다.", content = @Content(mediaType = "application/json")),
		@ApiResponse(responseCode = "403", description = "접근 권한이 없습니다.", content = @Content(mediaType = "application/json")),
		@ApiResponse(responseCode = "500", description = "서버 에러가 발생하였습니다.", content = @Content(mediaType = "application/json"))
	})
	public ResponseEntity<TokenDto.CreateAccessTokenResponse> createToken(
		@RequestBody TokenDto.CreateAccessTokenRequest request
	) {
		String newAccessToken = tokenService.createNewAccessToken(request.refreshToken());
		return ResponseEntity.status(HttpStatus.CREATED).body(new TokenDto.CreateAccessTokenResponse(newAccessToken));
	}
}
