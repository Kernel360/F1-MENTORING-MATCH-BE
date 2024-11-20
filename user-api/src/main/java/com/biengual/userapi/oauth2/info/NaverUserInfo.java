package com.biengual.userapi.oauth2.info;

import java.util.HashMap;
import java.util.Map;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class NaverUserInfo implements OAuth2UserInfo {
	private final Map<String, Object> naverAccount;

	@Override
	public String getProviderId() {
		Map<String, Object> accountInfo = (HashMap<String, Object>) naverAccount.get("response");

		return String.valueOf(accountInfo.get("id"));
	}

	@Override
	public String getProvider() {
		return "naver";
	}

	@Override
	public String getEmail() {
		Map<String, Object> accountInfo = (HashMap<String, Object>)naverAccount.get("response");
		return String.valueOf(accountInfo.get("email"));
	}

	@Override
	public String getUsername() {
		Map<String, Object> accountInfo = (HashMap<String, Object>)naverAccount.get("response");
		return String.valueOf(accountInfo.get("name"));
	}

	@Override
	public Map<String, Object> getAttributes() {
		return naverAccount;
	}

}