package com.biengual.userapi.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.CreateBucketRequest;
import software.amazon.awssdk.services.s3.model.HeadBucketRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;

@Profile("local")
@Configuration
@RequiredArgsConstructor
public class LocalStackImageConfig {
    private final S3Client s3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    @PostConstruct
    public void createBucket() {
        if(!this.doesBucketExist(bucketName)) {
            CreateBucketRequest createBucketRequest = CreateBucketRequest.builder()
                .bucket(bucketName)
                .build();
            s3Client.createBucket(createBucketRequest);
        }
    }

    private boolean doesBucketExist(String bucketName) {
        try {
            s3Client.headBucket(HeadBucketRequest.builder().bucket(bucketName).build());
            return true;
        } catch (S3Exception e) {
            if (e.statusCode() == 404) {
                return false;
            }
            throw e;
        }
    }
}
