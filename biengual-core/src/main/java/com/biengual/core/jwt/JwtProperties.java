package com.biengual.core.jwt;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Component
@ConfigurationProperties("jwt")
public class JwtProperties {
	private String issuer;
	private String secret;
}
