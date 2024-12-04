package com.biengual.userapi.image.domain;

public interface ImageReader {
    String getImageFromS3(Long contentId);
}
