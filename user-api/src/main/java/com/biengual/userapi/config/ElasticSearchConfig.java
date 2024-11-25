package com.biengual.userapi.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchConfiguration;

import org.springframework.beans.factory.annotation.Value;


@Configuration
public class ElasticSearchConfig  extends ElasticsearchConfiguration {
    @Value("${spring.elasticsearch.username}")
    private String username;

    @Value("${spring.elasticsearch.password}")
    private String password;

    @Value("${spring.elasticsearch.uris}")
    private String[] esHost;

    @Override
    public ClientConfiguration clientConfiguration() {
        return ClientConfiguration.builder()
            .connectedTo(esHost)
            .withBasicAuth(username, password)
            .build();
    }
    }