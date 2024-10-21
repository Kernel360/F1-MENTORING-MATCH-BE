package com.biengual.userapi.oauth2.repository;

import com.biengual.userapi.util.CookieUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.util.WebUtils;

@Component
@RequiredArgsConstructor
public class OAuth2AuthorizationRequestBasedOnCookieRepository
	implements AuthorizationRequestRepository<OAuth2AuthorizationRequest> {
	private final CookieUtil cookieUtil;

	@Override
	public OAuth2AuthorizationRequest loadAuthorizationRequest(HttpServletRequest request) {

		Cookie cookie = WebUtils.getCookie(request, CookieUtil.OAUTH2_AUTHORIZATION_REQUEST_NAME);
		if(cookie == null) {
			return null;
		}

		return cookieUtil.deserialize(cookie, OAuth2AuthorizationRequest.class);
	}

	@Override
	public void saveAuthorizationRequest(OAuth2AuthorizationRequest authorizationRequest, HttpServletRequest request,
		HttpServletResponse response) {

		if(authorizationRequest == null) {
			removeCookies(request, response);
			return;
		}

		cookieUtil.addOAuth2AuthorizationRequestCookie(request, response, authorizationRequest);

		cookieUtil.addReturnUrlCookie(request, response);
	}

	@Override
	public OAuth2AuthorizationRequest removeAuthorizationRequest(HttpServletRequest request,
		HttpServletResponse response) {

		return this.loadAuthorizationRequest(request);
	}

	public void removeCookies(HttpServletRequest request, HttpServletResponse response) {

		cookieUtil.removeOAuth2AuthorizationRequestCookie(request, response);

		cookieUtil.removeReturnUrlCookie(request, response);
	}
}
