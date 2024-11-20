package com.biengual.userapi.oauth2.info;

import java.util.LinkedHashMap;
import java.util.Map;

import lombok.AllArgsConstructor;

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
		return String.valueOf(accountInfo.get("email"));
	}

	@Override
	public String getUsername() {
		LinkedHashMap<String, Object> accountInfo = (LinkedHashMap<String, Object>)kakaoAccount.get("kakao_account");
		return String.valueOf(((LinkedHashMap<String, Object>)accountInfo.get("profile")).get("nickname"));
	}

	@Override
	public Map<String, Object> getAttributes() {
		return kakaoAccount;
	}

}