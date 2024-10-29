package com.biengual.userapi.oauth2.handler;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.WebUtils;

import com.biengual.core.annotation.LoginLogging;
import com.biengual.core.domain.entity.user.UserEntity;
import com.biengual.core.enums.UserStatus;
import com.biengual.core.response.error.exception.CommonException;
import com.biengual.core.util.CookieUtil;
import com.biengual.core.util.HttpServletResponseUtil;
import com.biengual.userapi.oauth2.info.OAuth2UserPrincipal;
import com.biengual.userapi.oauth2.repository.OAuth2AuthorizationRequestBasedOnCookieRepository;
import com.biengual.userapi.token.service.RefreshTokenService;
import com.biengual.userapi.token.service.TokenProvider;
import com.biengual.userapi.user.domain.UserService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {
	private final TokenProvider tokenProvider;
	private final RefreshTokenService refreshTokenService;
	private final UserService userService;
	private final CookieUtil cookieUtil;
	private final OAuth2AuthorizationRequestBasedOnCookieRepository oAuth2AuthorizationRequestBasedOnCookieRepository;

	@Value("${spring.security.oauth2.success.redirect-uri}")
	public String oAuth2SuccessRedirectBaseUri;

	private static final String FIRST_LOGIN_REDIRECT_URL = "/login/add";

	@Override
	@LoginLogging
	public void onAuthenticationSuccess(
		HttpServletRequest request, HttpServletResponse response, Authentication authentication
	) throws IOException {
		try {
			OAuth2UserPrincipal principal = (OAuth2UserPrincipal)authentication.getPrincipal();

			UserEntity user = userService.getUserByOAuthUser(principal);

			String refreshToken = tokenProvider.generateRefreshToken(user);
			cookieUtil.addRefreshTokenCookie(request, response, refreshToken);

			String accessToken = tokenProvider.generateAccessToken(user);
			cookieUtil.addAccessTokenCookie(request, response, accessToken);

			refreshTokenService.saveRefreshToken(user, refreshToken);

			// OAtuh 인증 서버에서 발급받은 access token과 return url 정보 쿠키에서 삭제
			oAuth2AuthorizationRequestBasedOnCookieRepository.removeCookies(request, response);

			Cookie returnUrlCookie = WebUtils.getCookie(request, CookieUtil.RETURN_URL_NAME);

			response.sendRedirect(getRedirectUrlWithReturnUrlCookie(user, returnUrlCookie));

		} catch (CommonException e) {
			log.error(e.getErrorCode().getCode() + " : " + e.getErrorCode().getMessage());

			HttpServletResponseUtil.createErrorResponse(response, e);
		}

		// TODO: 나머지 Exception에 대한 응답 컨벤션에 따라 추가될 수 있음 ex) Server Internal Error
	}

	// Internal Methods=================================================================================================

	// 최종 리다이렉트 URL 구하는 메서드
	private String getRedirectUrlWithReturnUrlCookie(UserEntity user, Cookie cookie) {
		String redirectUrl = oAuth2SuccessRedirectBaseUri;

		// 첫 로그인 시 추가되는 기본 URL
		if (user.getUserStatus() == UserStatus.USER_STATUS_CREATED) {
			redirectUrl += FIRST_LOGIN_REDIRECT_URL;
		}

		// returnUrl 쿠키가 존재할 때 추가되는 URL
		if (cookie != null) {
			String additionalUrl = user.getUserStatus() == UserStatus.USER_STATUS_CREATED
				? "?" + cookie.getName() + "=" + cookie.getValue()
				: cookie.getValue();

			redirectUrl += additionalUrl;
		}

		return redirectUrl;
	}
}
