package com.biengual.userapi.config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@Configuration
public class YoutubeAppConfig {

	@Bean
	public RestTemplate restTemplate() {
		HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
		factory.setConnectTimeout(5000);  // 연결 타임아웃 (밀리초)
		factory.setConnectionRequestTimeout(5000);     // 읽기 타임아웃 (밀리초)

		return new RestTemplate(factory);
	}
}