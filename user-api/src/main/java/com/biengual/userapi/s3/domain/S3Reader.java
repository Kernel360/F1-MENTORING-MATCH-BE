package com.biengual.userapi.s3.domain;

public interface S3Reader {
    String getImageFromS3(String bucket, Long contentId, int size);
}
