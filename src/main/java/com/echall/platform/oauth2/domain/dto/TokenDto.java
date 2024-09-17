package com.echall.platform.oauth2.domain.dto;

public class TokenDto {

	public record CreateAccessTokenResponse(
		String accessToken
	) {
	}
	public record CreateAccessTokenRequest(
		String refreshToken
	) {
	}
}
