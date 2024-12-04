package com.biengual.userapi.s3.infrastructure;

import static com.biengual.core.constant.ServiceConstant.*;

import java.net.URI;

import org.springframework.beans.factory.annotation.Value;

import com.biengual.core.annotation.DataProvider;
import com.biengual.userapi.s3.domain.S3Reader;

import lombok.RequiredArgsConstructor;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetUrlRequest;

@DataProvider
@RequiredArgsConstructor
public class S3ReaderImpl implements S3Reader {
    private final S3Client s3Client;
    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;
    @Value("${cloud.aws.cloudfront.domain}")
    private String cloudfrontDomain;
    @Value("${spring.profiles.active:}")
    private String activeProfile;

    @Override
    public String getImageFromS3(Long contentId) {
        String key = this.generateKey(contentId);
        if (activeProfile.equals("local")) {  // Local 은 CDN 사용 X
            GetUrlRequest getUrlRequest = GetUrlRequest.builder()
                .bucket(bucketName)
                .key(key)
                .build();
            return s3Client.utilities().getUrl(getUrlRequest).toString();
        }

        return URI.create("https://" + cloudfrontDomain).resolve(key).toString();
    }

    // Internal Methods ================================================================================================
    private String generateKey(Long contentId) {
        return "content-" + contentId + "/size-" + IMAGE_RESIZED_SIZE + ".webp";
    }
}
