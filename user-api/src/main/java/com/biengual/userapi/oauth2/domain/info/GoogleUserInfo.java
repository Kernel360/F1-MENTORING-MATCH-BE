package com.biengual.userapi.oauth2.domain.info;

import lombok.AllArgsConstructor;

import java.util.Map;

@AllArgsConstructor
public class GoogleUserInfo implements OAuth2UserInfo {
	private final Map<String, Object> attributes; // oauth2User.getAttributes()

	@Override
	public String getProviderId() {
		return String.valueOf(attributes.get("sub"));
	}

	@Override
	public String getProvider() {
		return "google";
	}

	@Override
	public String getEmail() {
		return String.valueOf(attributes.get("email"));
	}

	@Override
	public String getUsername() {
		return String.valueOf(attributes.get("name"));
	}

	@Override
	public Map<String, Object> getAttributes() {
		return attributes;
	}
}
