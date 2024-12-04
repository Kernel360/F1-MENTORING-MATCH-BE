package com.biengual.userapi.image.infrastructure;

import static com.biengual.core.constant.ServiceConstant.*;
import static com.biengual.core.response.error.code.ImageErrorCode.*;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;

import javax.imageio.ImageIO;

import org.springframework.beans.factory.annotation.Value;

import com.biengual.core.annotation.DataProvider;
import com.biengual.core.domain.entity.content.ContentEntity;
import com.biengual.core.response.error.exception.CommonException;
import com.biengual.userapi.content.domain.ContentCustomRepository;
import com.biengual.userapi.content.domain.ContentRepository;
import com.biengual.userapi.image.domain.ImageReader;
import com.biengual.userapi.image.domain.ImageStore;

import lombok.RequiredArgsConstructor;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;

@DataProvider
@RequiredArgsConstructor
public class ImageStoreImpl implements ImageStore {
    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    private final S3Client s3Client;
    private final ImageReader imageReader;      // TODO: 삭제 예정
    private final ContentRepository contentRepository;
    private final ContentCustomRepository contentCustomRepository;

    @Override
    public void saveImageToS3(Long contentId) {
        String thumbnailUrl = contentCustomRepository.findThumbnailUrlById(contentId);
        Path tempFile = null;
        try {
            URL url = new URL(thumbnailUrl);
            tempFile = Files.createTempFile("temp", ".webp");
            Files.copy(url.openStream(), tempFile, StandardCopyOption.REPLACE_EXISTING);
            this.uploadToS3(contentId, tempFile);
            Files.delete(tempFile);
        } catch (IOException e) {
            throw new CommonException(IMAGE_STORE_FAILURE);
        }
    }

    // TODO: PROD 까지 적용 후 관련 메서드 모두 삭제 예정
    @Override
    public void saveAllImagesToS3() {
        List<ContentEntity> contents = contentRepository.findAll();
        for(ContentEntity content : contents) {
            this.saveImageToS3(content.getId());
            content.updateS3Url(imageReader.getImageFromS3(content.getId()));
        }
    }

    // Internal Methods ================================================================================================
    private void uploadToS3(Long contentId, Path tempFile) {
        // 비율에 맞게 변환된 이미지 생성
        byte[] resizedImage = this.convertAndResizeImage(tempFile.toFile());

        // S3 업로드
        String key = this.generateKey(contentId);

        s3Client.putObject(builder -> builder
                .bucket(bucketName)
                .key(key)
                .cacheControl("max-age=7200")
                .contentType("image/webp"),
            RequestBody.fromBytes(resizedImage)
        );
    }

    private byte[] convertAndResizeImage(File inputFile) {
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
        double scale = Math.min((double)IMAGE_RESIZED_SIZE / originalWidth, (double)IMAGE_RESIZED_SIZE / originalHeight);
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

    private String generateKey(Long contentId) {
        return "content-" + contentId + "/size-" + IMAGE_RESIZED_SIZE + ".webp";
    }
}
