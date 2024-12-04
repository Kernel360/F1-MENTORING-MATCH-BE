package com.biengual.userapi.s3.domain;

public interface S3Store {
    void putImageToS3(Long contentId);
}
