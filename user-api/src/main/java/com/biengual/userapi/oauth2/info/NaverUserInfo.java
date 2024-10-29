package com.biengual.userapi.oauth2.info;

import lombok.AllArgsConstructor;

import java.util.LinkedHashMap;
import java.util.Map;

@AllArgsConstructor
public class NaverUserInfo implements OAuth2UserInfo {
	private final Map<String, Object> naverAccount;

	@Override
	public String getProviderId() {
		LinkedHashMap<String, Object> accountInfo = (LinkedHashMap<String, Object>) naverAccount.get("response");

		return String.valueOf(accountInfo.get("id"));
	}

	@Override
	public String getProvider() {
		return "naver";
	}

	@Override
	public String getEmail() {
		LinkedHashMap<String, Object> accountInfo = (LinkedHashMap<String, Object>)naverAccount.get("response");
		return String.valueOf(accountInfo.get("email"));
	}

	@Override
	public String getUsername() {
		LinkedHashMap<String, Object> accountInfo = (LinkedHashMap<String, Object>)naverAccount.get("response");
		return String.valueOf(accountInfo.get("name"));
	}

	@Override
	public Map<String, Object> getAttributes() {
		return naverAccount;
	}

}