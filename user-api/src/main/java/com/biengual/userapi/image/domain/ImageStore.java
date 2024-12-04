package com.biengual.userapi.image.domain;

public interface ImageStore {
    void saveImage(Long contentId);

    void saveAllImagesToS3();
}
