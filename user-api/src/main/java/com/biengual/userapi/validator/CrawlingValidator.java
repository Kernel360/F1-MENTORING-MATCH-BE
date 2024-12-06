package com.biengual.userapi.validator;

import static com.biengual.core.constant.CrawlingAutomationConstant.*;
import static com.biengual.core.response.error.code.CrawlingErrorCode.*;

import java.time.Duration;

import com.biengual.core.annotation.Validator;
import com.biengual.core.response.error.exception.CommonException;
import com.biengual.userapi.content.domain.ContentCustomRepository;

import lombok.RequiredArgsConstructor;

@Validator
@RequiredArgsConstructor
public class CrawlingValidator {
    private final ContentCustomRepository contentCustomRepository;

    // 이미 크롤링한 url 인지 확인
    public void verifyCrawling(String url) {
        if (contentCustomRepository.existsByUrl(url)) {
            throw new CommonException(CRAWLING_ALREADY_DONE);
        }
    }

    public boolean verifyCrawlingUrlAlreadyExists(String url) {
        return contentCustomRepository.existsByUrl(url);
    }

    public boolean verifyCrawlingYoutubeDuration(Duration duration) {
        return duration.compareTo(Duration.ofMinutes(MAX_YOUTUBE_DURATION)) <= 0;
    }
}
