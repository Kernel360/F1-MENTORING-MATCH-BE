package com.biengual.userapi.s3.infrastructure;

import org.springframework.beans.factory.annotation.Value;

import com.biengual.core.annotation.DataProvider;
import com.biengual.userapi.s3.domain.S3Reader;

import lombok.RequiredArgsConstructor;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetUrlRequest;

@DataProvider
@RequiredArgsConstructor
public class S3ReaderImpl implements S3Reader {
    @Value("${localstack.bucket-name}")
    private String bucketName;

    private final S3Client s3Client;

    @Override
    public String getImageFromS3(Long contentId, int size) {
        String key = this.generateKey(contentId, size);
        GetUrlRequest getUrlRequest = GetUrlRequest.builder()
            .bucket(bucketName)
            .key(key)
            .build();
        // InvocationTargetException : no such key
        return s3Client.utilities().getUrl(getUrlRequest).toString();
    }

    // Internal Methods ================================================================================================
    private String generateKey(Long contentId, int size) {
        return "content-" + contentId + "/size-" + size + ".webp";
    }
}
