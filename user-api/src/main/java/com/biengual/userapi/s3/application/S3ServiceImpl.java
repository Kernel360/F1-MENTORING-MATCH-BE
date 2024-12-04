package com.biengual.userapi.s3.application;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.biengual.userapi.s3.domain.S3Reader;
import com.biengual.userapi.s3.domain.S3Service;
import com.biengual.userapi.s3.domain.S3Store;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class S3ServiceImpl implements S3Service {
    @Value("${localstack.bucket-name}")
    private String bucketName;

    private final S3Store s3Store;
    private final S3Reader s3Reader;

    @Override
    @Transactional
    public void saveToS3(Long contentId) {
        s3Store.putImageToS3(bucketName, contentId);
    }

    @Override
    @Transactional(readOnly = true)
    public String getImageFromS3(Long contentId) {
        return s3Reader.getImageFromS3(bucketName, contentId, 480);
    }
}
