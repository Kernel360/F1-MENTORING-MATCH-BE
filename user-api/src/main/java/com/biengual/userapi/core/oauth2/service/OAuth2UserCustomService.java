package com.biengual.userapi.core.oauth2.service;

import com.biengual.userapi.core.oauth2.domain.info.OAuth2UserInfo;
import com.biengual.userapi.core.oauth2.domain.info.OAuth2UserInfoFactory;
import com.biengual.userapi.core.oauth2.domain.info.OAuth2UserPrincipal;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
public class OAuth2UserCustomService extends DefaultOAuth2UserService {

	@Override
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
		OAuth2User user = super.loadUser(userRequest);

		String provider = userRequest.getClientRegistration().getRegistrationId();

		OAuth2UserInfo oAuth2UserInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(provider, user.getAttributes());

        return OAuth2UserPrincipal.from(oAuth2UserInfo);
	}
}
