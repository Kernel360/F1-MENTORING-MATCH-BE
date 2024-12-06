package com.biengual.userapi.image.domain;

public interface ImageService {
    void save(Long contentId);

    String getImage(Long contentId);

    void saveAllToS3();
}
