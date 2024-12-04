package com.biengual.userapi.s3.domain;

public interface S3Service {
    void saveToS3(Long contentId);
    String getImageFromS3(Long contentId);
}
