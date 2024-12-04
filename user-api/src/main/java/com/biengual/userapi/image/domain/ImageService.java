package com.biengual.userapi.image.domain;

public interface ImageService {
    void saveToS3(Long contentId);

    String getImageFromS3(Long contentId);

    void saveAllToS3();
}
