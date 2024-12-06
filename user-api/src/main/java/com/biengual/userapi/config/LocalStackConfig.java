package com.biengual.userapi.config;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProviderChain;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3Configuration;

@Profile("local")
@Configuration
public class LocalStackConfig {
    @Value("${cloud.aws.endpoint}")
    private String awsEndpoint;

    @Value("${localstack.access-key}")
    private String accessKey;
    @Value("${localstack.secret-key}")
    private String secretKey;

    @Bean
    public AwsCredentialsProvider awsCredentialsProvider() {
        return AwsCredentialsProviderChain.builder()
            .reuseLastProviderEnabled(true)
            .credentialsProviders(
                List.of(
                    DefaultCredentialsProvider.create(),
                    StaticCredentialsProvider.create(AwsBasicCredentials.create(accessKey, secretKey))
                )
            )
            .build();
    }

    @Bean
    public S3Client s3Client() {
        return S3Client.builder()
            .credentialsProvider(awsCredentialsProvider())
            .region(Region.US_EAST_1)
            .endpointOverride(URI.create(awsEndpoint))
            .serviceConfiguration(
                S3Configuration.builder()
                    .pathStyleAccessEnabled(false)  // endPoint 가 http://localhost:4566 면 true로 수정해야 함
                    .build()
            )
            .build();
    }
}