package com.biengual.userapi.image.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.biengual.userapi.image.domain.ImageReader;
import com.biengual.userapi.image.domain.ImageService;
import com.biengual.userapi.image.domain.ImageStore;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {
    private final ImageStore imageStore;
    private final ImageReader imageReader;

    @Override
    @Transactional
    public void saveToS3(Long contentId) {
        imageStore.saveImageToS3(contentId);
    }

    @Override
    @Transactional(readOnly = true)
    public String getImageFromS3(Long contentId) {
        return imageReader.getImageFromS3(contentId);
    }

    @Override
    @Transactional
    public void saveAllToS3() {
        imageStore.saveAllImagesToS3();
    }
}
