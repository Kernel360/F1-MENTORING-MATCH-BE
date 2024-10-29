package com.biengual.userapi.oauth2.info;

import java.util.Map;

public interface OAuth2UserInfo {
	String getProviderId();

	String getProvider();

	String getEmail();

	String getUsername();

	Map<String, Object> getAttributes();
}
