package com.biengual.userapi.s3.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.biengual.userapi.s3.domain.S3Reader;
import com.biengual.userapi.s3.domain.S3Service;
import com.biengual.userapi.s3.domain.S3Store;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class S3ServiceImpl implements S3Service {
    private final S3Store s3Store;
    private final S3Reader s3Reader;

    @Override
    @Transactional
    public void saveToS3(Long contentId) {
        s3Store.saveImageToS3(contentId);
    }

    @Override
    @Transactional(readOnly = true)
    public String getImageFromS3(Long contentId) {
        return s3Reader.getImageFromS3(contentId);
    }

    @Override
    @Transactional
    public void saveAllToS3() {
        s3Store.saveAllImagesToS3();
    }
}
