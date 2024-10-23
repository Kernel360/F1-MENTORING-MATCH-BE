package com.biengual.userapi.oauth2;

import com.biengual.userapi.annotation.LoginLogging;
import com.biengual.userapi.message.error.exception.CommonException;
import com.biengual.userapi.oauth2.domain.info.OAuth2UserPrincipal;
import com.biengual.userapi.oauth2.repository.OAuth2AuthorizationRequestBasedOnCookieRepository;
import com.biengual.userapi.oauth2.service.RefreshTokenService;
import com.biengual.userapi.user.domain.UserService;
import com.biengual.userapi.user.domain.UserEntity;
import com.biengual.userapi.util.CookieUtil;
import com.biengual.userapi.util.HttpServletResponseUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.WebUtils;

import java.io.IOException;

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

	@Override
	@LoginLogging
	public void onAuthenticationSuccess(
		HttpServletRequest request, HttpServletResponse response, Authentication authentication
	) throws IOException {
		try {
			OAuth2UserPrincipal oAuth2UserPrincipal = (OAuth2UserPrincipal)authentication.getPrincipal();

			UserEntity user = userService.getUserByOAuthUser(oAuth2UserPrincipal);

			String refreshToken = tokenProvider.generateRefreshToken(user);
			cookieUtil.addRefreshTokenCookie(request, response, refreshToken);

			String accessToken = tokenProvider.generateAccessToken(user);
			cookieUtil.addAccessTokenCookie(request, response, accessToken);

			refreshTokenService.saveRefreshToken(user, refreshToken);

			oAuth2AuthorizationRequestBasedOnCookieRepository.removeCookies(request, response);

			Cookie cookie = WebUtils.getCookie(request, CookieUtil.RETURN_URL_NAME);
			if (cookie != null) {
				response.sendRedirect(oAuth2SuccessRedirectBaseUri + cookie.getValue());
			} else {
				response.sendRedirect(oAuth2SuccessRedirectBaseUri);
			}


		} catch (CommonException e) {
			log.error(e.getErrorCode().getCode() + " : " + e.getErrorCode().getMessage());

			HttpServletResponseUtil.createErrorResponse(response, e);
		}

		// TODO: 나머지 Exception에 대한 응답 컨벤션에 따라 추가될 수 있음 ex) Server Internal Error
	}
}
