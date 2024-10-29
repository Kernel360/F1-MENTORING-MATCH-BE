package com.biengual.userapi.oauth2.info;

import java.util.Map;

import com.biengual.core.enums.OAuth2Provider;
import com.biengual.core.response.error.exception.OAuth2AuthenticationProcessingException;

// TODO: OAuth2AuthenticationProcessingException 로직은 추후 변경될 수 있음
public class OAuth2UserInfoFactory {

    public static OAuth2UserInfo getOAuth2UserInfo(String registrationId, Map<String, Object> attributes) {
        if (OAuth2Provider.GOOGLE.getRegistrationId().equals(registrationId)) {
            return new GoogleUserInfo(attributes);
        } else if (OAuth2Provider.NAVER.getRegistrationId().equals(registrationId)) {
            return new NaverUserInfo(attributes);
        } else if (OAuth2Provider.KAKAO.getRegistrationId().equals(registrationId)) {
            return new KakaoUserInfo(attributes);
        } else {
            throw new OAuth2AuthenticationProcessingException("Login with " + registrationId + " is not supported");
        }
    }
}
