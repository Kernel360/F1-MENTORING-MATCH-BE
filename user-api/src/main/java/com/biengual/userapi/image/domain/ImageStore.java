package com.biengual.userapi.image.domain;

public interface ImageStore {
    void saveImageToS3(Long contentId);

    void saveAllImagesToS3();
}
