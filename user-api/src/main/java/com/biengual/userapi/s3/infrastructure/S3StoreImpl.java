package com.biengual.userapi.s3.infrastructure;

import static com.biengual.core.response.error.code.S3ErrorCode.*;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

import javax.imageio.ImageIO;

import org.springframework.beans.factory.annotation.Value;

import com.biengual.core.annotation.DataProvider;
import com.biengual.core.response.error.exception.CommonException;
import com.biengual.userapi.content.domain.ContentCustomRepository;
import com.biengual.userapi.s3.domain.S3Store;

import lombok.RequiredArgsConstructor;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;

@DataProvider
@RequiredArgsConstructor
public class S3StoreImpl implements S3Store {
    @Value("${localstack.bucket-name}")
    private String bucketName;

    private final S3Client s3Client;
    private final ContentCustomRepository contentCustomRepository;

    @Override
    public void putImageToS3(Long contentId) {
        String thumbnailUrl = contentCustomRepository.findThumbnailUrlById(contentId);
        Path tempFile = null;
        try {
            URL url = new URL(thumbnailUrl);
            tempFile = Files.createTempFile("temp", ".webp");
            Files.copy(url.openStream(), tempFile, StandardCopyOption.REPLACE_EXISTING);
            this.uploadToS3(bucketName, contentId, tempFile);
            Files.delete(tempFile);

        } catch (IOException e) {
            throw new CommonException(S3_STORE_FAILURE);
        }
    }

    // Internal Methods ================================================================================================
    private void uploadToS3(String bucket, Long contentId, Path tempFile) {
        int[] sizes = {360, 480};
        for (int maxSize : sizes) {
            // 비율에 맞게 변환된 이미지 생성
            byte[] resizedImage = this.convertAndResizeImage(tempFile.toFile(), maxSize);

            // S3 업로드
            String key = this.generateKey(contentId, maxSize); // maxSize로 구분
            s3Client.putObject(builder -> builder
                    .bucket(bucket)
                    .key(key)
                    .contentType("image/webp"),
                RequestBody.fromBytes(resizedImage)
            );
        }
    }

    private byte[] convertAndResizeImage(File inputFile, int maxSize) {
        BufferedImage originalImage = null;
        try {
            originalImage = ImageIO.read(inputFile);
            if (originalImage == null) {
                throw new IOException("Failed to read image file");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // 원본 크기 및 비율 계산
        int originalWidth = originalImage.getWidth();
        int originalHeight = originalImage.getHeight();
        double scale = Math.min((double)maxSize / originalWidth, (double)maxSize / originalHeight);
        int targetWidth = (int)(originalWidth * scale);
        int targetHeight = (int)(originalHeight * scale);

        // 다단계 리사이징
        BufferedImage resizedImage = originalImage;
        while (resizedImage.getWidth() / 2 > targetWidth && resizedImage.getHeight() / 2 > targetHeight) {
            resizedImage =
                this.scaleImage(
                    resizedImage,
                    resizedImage.getWidth() / 2,
                    resizedImage.getHeight() / 2
                );
        }
        resizedImage = this.scaleImage(resizedImage, targetWidth, targetHeight);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            ImageIO.write(resizedImage, "webp", baos);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return baos.toByteArray();
    }

    private BufferedImage scaleImage(BufferedImage img, int targetWidth, int targetHeight) {
        BufferedImage resizedImage = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = resizedImage.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        g.drawImage(img, 0, 0, targetWidth, targetHeight, null);
        g.dispose();
        return resizedImage;
    }

    private String generateKey(Long contentId, int size) {
        return "content-" + contentId + "/size-" + size + ".webp";
    }
}
