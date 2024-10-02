package com.echall.platform.config;

import com.echall.platform.oauth2.OAuth2SuccessHandler;
import com.echall.platform.oauth2.TokenProvider;
import com.echall.platform.oauth2.service.OAuth2UserCustomService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HttpBasicConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.CorsUtils;

import java.util.Collections;
import java.util.List;

@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

	private final OAuth2UserCustomService oAuth2UserCustomService;
	private final TokenProvider tokenProvider;
	private final OAuth2SuccessHandler oAuth2SuccessHandler;

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

		http
			.formLogin(AbstractHttpConfigurer::disable)
			.logout(AbstractHttpConfigurer::disable)
			.httpBasic(HttpBasicConfigurer::disable)
			.csrf(AbstractHttpConfigurer::disable)
			.cors(cors -> cors.configurationSource(corsConfigurationSource()));

		http
			.sessionManagement((session) -> session
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

		http
			.addFilterBefore(tokenAuthenticationFilter(),
				UsernamePasswordAuthenticationFilter.class)
			.authorizeHttpRequests(authorize -> {
				authorize
					// Can access from USER
					.requestMatchers("/api/b1/**").hasAnyRole("USER", "DEVELOPER")
					.requestMatchers("/api/u1/**").hasAnyRole("USER", "DEVELOPER")
					.requestMatchers("/api/p1/**").hasAnyRole("USER", "DEVELOPER")
					.requestMatchers("/api/t1/**").hasAnyRole("USER", "DEVELOPER")

					// Can access from ADMIN
					.requestMatchers("/api/a1/**").hasAnyRole("ADMIN", "DEVELOPER")
					.requestMatchers("/api/c1/**").hasAnyRole("ADMIN", "DEVELOPER")

					/**
					 * Need To Activate DEVELOPER on DEPLOY SETTING
					 */
					// Can access only DEVELOPER
					.requestMatchers("/api/token").hasRole("DEVELOPER")
					.requestMatchers("/swagger-ui/**").hasRole("DEVELOPER")
					.requestMatchers("/api-info/**").hasRole("DEVELOPER")


					.requestMatchers(CorsUtils::isPreFlightRequest).permitAll()
					.anyRequest().permitAll();
			});

		http
			.oauth2Login(oauth2 -> oauth2
				.loginPage("/login")
				.userInfoEndpoint(userInfoEndpoint -> userInfoEndpoint
					.userService(oAuth2UserCustomService)
				)
				.successHandler(oAuth2SuccessHandler)

			);
		http
			.exceptionHandling(exceptionHandling -> exceptionHandling
				.defaultAuthenticationEntryPointFor(
					new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED),
					new AntPathRequestMatcher("/api/**")
				)
			);

		return http.build();
	}

	@Bean
	public TokenAuthenticationFilter tokenAuthenticationFilter() {
		return new TokenAuthenticationFilter(tokenProvider);
	}

	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		return request -> {
			CorsConfiguration corsConfiguration = new CorsConfiguration();
			corsConfiguration.setAllowedOrigins(List.of("http://localhost:3000"));
			corsConfiguration.setAllowedHeaders(Collections.singletonList("*"));
			corsConfiguration.setExposedHeaders(Collections.singletonList("*"));
			corsConfiguration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
			corsConfiguration.setAllowedOriginPatterns(Collections.singletonList("http://localhost:3000"));
			corsConfiguration.setAllowCredentials(true);
			return corsConfiguration;
		};

	}
}