package com.biengual.core.util;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.SerializationUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.time.Duration;
import java.util.Base64;


@Component
public class CookieUtil {
	public static final String ACCESS_TOKEN_NAME = "access_token";
	public static final String REFRESH_TOKEN_NAME = "refresh_token";
	public static final String OAUTH2_AUTHORIZATION_REQUEST_NAME = "oauth2_authorization_request";
	public static final String RETURN_URL_NAME = "returnUrl";
	public static final Duration ACCESS_TOKEN_COOKIE_EXPIRE = Duration.ofDays(1);
	public static final Duration REFRESH_TOKEN_COOKIE_EXPIRE = Duration.ofDays(7);
	public static final Duration OAUTH2_AUTHORIZATION_REQUEST_COOKIE_EXPIRE = Duration.ofMinutes(3);
	public static final Duration RETURN_URL_NAME_COOKIE_EXPIRE = Duration.ofMinutes(3);

	@Value("${spring.profiles.active}")
	private String activeProfile;

	public void addAccessTokenCookie(HttpServletRequest request, HttpServletResponse response, String accessToken) {
		removeCookie(request, response, ACCESS_TOKEN_NAME);
		addCookie(response, ACCESS_TOKEN_NAME, accessToken, (int) ACCESS_TOKEN_COOKIE_EXPIRE.toSeconds());
	}

	public void addRefreshTokenCookie(HttpServletRequest request, HttpServletResponse response, String refreshToken) {
		removeCookie(request, response, REFRESH_TOKEN_NAME);
		addCookie(response, REFRESH_TOKEN_NAME, refreshToken, (int) REFRESH_TOKEN_COOKIE_EXPIRE.toSeconds());
	}

	public void addOAuth2AuthorizationRequestCookie(HttpServletRequest request, HttpServletResponse response,
													OAuth2AuthorizationRequest oAuth2AuthorizationRequest) {
		removeCookie(request, response, OAUTH2_AUTHORIZATION_REQUEST_NAME);
		addCookie(response, OAUTH2_AUTHORIZATION_REQUEST_NAME, serialize(oAuth2AuthorizationRequest),
			(int) OAUTH2_AUTHORIZATION_REQUEST_COOKIE_EXPIRE.toSeconds());
	}

	public void addReturnUrlCookie(HttpServletRequest request, HttpServletResponse response) {
		removeCookie(request, response, RETURN_URL_NAME);
		addCookie(response, RETURN_URL_NAME, request.getParameter(RETURN_URL_NAME),
			(int) RETURN_URL_NAME_COOKIE_EXPIRE.toSeconds());
	}

	public void removeAccessTokenCookie(HttpServletRequest request, HttpServletResponse response) {
		removeCookie(request, response, ACCESS_TOKEN_NAME);
	}

	public void removeRefreshTokenCookie(HttpServletRequest request, HttpServletResponse response) {
		removeCookie(request, response, REFRESH_TOKEN_NAME);
	}

	public void removeOAuth2AuthorizationRequestCookie(HttpServletRequest request, HttpServletResponse response) {
		removeCookie(request, response, OAUTH2_AUTHORIZATION_REQUEST_NAME);
	}

	public void removeReturnUrlCookie(HttpServletRequest request, HttpServletResponse response) {
		removeCookie(request, response, RETURN_URL_NAME);
	}

    // Internal Methods=================================================================================================
	// TODO: https 연결하고 검토 필요
	private void addCookie(HttpServletResponse response, String name, String value, int maxAge) {
		String cookieValue = createCookieValue(name, value, maxAge);

		response.addHeader("Set-Cookie", cookieValue);
	}

	private void removeCookie(HttpServletRequest request, HttpServletResponse response, String name) {
		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if (cookie.getName().equals(name)) {
					String cookieValue = createCookieValue(name, "", 0);

					response.addHeader("Set-Cookie", cookieValue);
				}
			}
		}
	}

	public String serialize(Object obj) {
		return Base64.getUrlEncoder()
			.encodeToString(SerializationUtils.serialize(obj));
	}

	public <T> T deserialize(Cookie cookie, Class<T> cls) {
		byte[] decodedBytes = Base64.getUrlDecoder().decode(cookie.getValue());

		try (
			 ObjectInputStream objectInputStream = new ObjectInputStream(new ByteArrayInputStream(decodedBytes))
		) {
			return cls.cast(objectInputStream.readObject());
		} catch (IOException | ClassNotFoundException e) {
			throw new IllegalArgumentException("Failed to deserialize object", e);
		}
	}

	private String createCookieValue(String name, String value, int maxAge) {
		ResponseCookie.ResponseCookieBuilder cookieBuilder = ResponseCookie.from(name, value)
			.httpOnly(true)
			.path("/")
			.maxAge(maxAge);

		if ("local".equals(activeProfile)) {
			cookieBuilder.secure(false)
				.domain(".localhost");
		}

		if ("dev".equals(activeProfile) || "prod".equals(activeProfile)) {
			cookieBuilder.secure(true)
				.sameSite("None")
				.domain(".biengual.store");
		}

		return cookieBuilder.build().toString();
	}
}
