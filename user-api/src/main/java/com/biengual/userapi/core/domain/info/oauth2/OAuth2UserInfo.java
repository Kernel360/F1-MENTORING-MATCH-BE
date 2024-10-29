package com.biengual.userapi.core.domain.info.oauth2;

import java.util.Map;

public interface OAuth2UserInfo {
	String getProviderId();

	String getProvider();

	String getEmail();

	String getUsername();

	Map<String, Object> getAttributes();
}
