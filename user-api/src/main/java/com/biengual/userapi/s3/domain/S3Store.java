package com.biengual.userapi.s3.domain;

public interface S3Store {
    void saveImageToS3(Long contentId);

    void saveAllImagesToS3();
}
