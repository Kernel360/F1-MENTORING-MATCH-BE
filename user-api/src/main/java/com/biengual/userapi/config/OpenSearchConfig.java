package com.biengual.userapi.config;

import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.opensearch.client.RestClient;
import org.opensearch.client.json.jackson.JacksonJsonpMapper;
import org.opensearch.client.opensearch.OpenSearchClient;
import org.opensearch.client.transport.rest_client.RestClientTransport;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class OpenSearchConfig {

    @Value("${opensearch.host}")
    private String host;

    @Value("${opensearch.port}")
    private Integer port;

    @Value("${opensearch.protocol}")
    private String protocol;

    @Value("${opensearch.username}")
    private String username;

    @Value("${opensearch.password}")
    private String password;

    @Bean
    public OpenSearchClient openSearchClient() {
        // 인증 제공자 설정
        BasicCredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        credentialsProvider.setCredentials(
            AuthScope.ANY,  // TODO: IAM 인증을 써서 보안 기능 추가해서 호스트와 포트 제한 필요
            new UsernamePasswordCredentials(username, password)
        );

        // RestClient 빌더에 인증 제공자 추가
        RestClient restClient = RestClient.builder(new HttpHost(host, port, protocol))
            .setHttpClientConfigCallback(
                httpAsyncClientBuilder
                    -> httpAsyncClientBuilder.setDefaultCredentialsProvider(credentialsProvider))
            .build();

        // OpenSearch 클라이언트 생성 및 반환
        RestClientTransport transport = new RestClientTransport(restClient, new JacksonJsonpMapper());
        return new OpenSearchClient(transport);
    }
}