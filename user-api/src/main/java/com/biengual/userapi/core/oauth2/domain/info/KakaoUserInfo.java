package com.biengual.userapi.core.oauth2.domain.info;

import lombok.AllArgsConstructor;

import java.util.LinkedHashMap;
import java.util.Map;

@AllArgsConstructor
public class KakaoUserInfo implements OAuth2UserInfo {
	private final Map<String, Object> kakaoAccount;

	@Override
	public String getProviderId() {
		return String.valueOf(this.kakaoAccount.get("id"));
	}

	@Override
	public String getProvider() {
		return "kakao";
	}

	@Override
	public String getEmail() {
		LinkedHashMap<String, Object> accountInfo = (LinkedHashMap<String, Object>)kakaoAccount.get("kakao_account");
		assert accountInfo != null;
		return String.valueOf(accountInfo.get("email"));
	}

	@Override
	public String getUsername() {
		return String.valueOf(kakaoAccount.get("username"));
	}

	@Override
	public Map<String, Object> getAttributes() {
		return kakaoAccount;
	}

}