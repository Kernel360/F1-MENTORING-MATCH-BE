package com.biengual.core.response.success;

import org.springframework.http.HttpStatus;

import com.biengual.core.response.status.RecommenderServiceStatus;
import com.biengual.core.response.status.ServiceStatus;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum RecommenderSuccessCode implements SuccessCode {
    RECOMMENDER_UPDATE_SUCCESS(HttpStatus.OK, RecommenderServiceStatus.RECOMMENDER_UPDATE_SUCCESS, "추천 시스템 업데이트 성공"),
    RECOMMENDER_CATEGORY_VIEW_SUCCESS(HttpStatus.OK, RecommenderServiceStatus.RECOMMENDER_CATEGORY_VIEW_SUCCESS, "카테고리 추천 컨텐츠 조회 성공"),
    RECOMMENDER_BOOKMARK_VIEW_SUCCESS(HttpStatus.OK, RecommenderServiceStatus.RECOMMENDER_BOOKMARK_VIEW_SUCCESS, "북마크 추천 컨텐츠 조회 성공")
    ;

    private final HttpStatus code;
    private final ServiceStatus serviceStatus;
    private final String message;

    @Override
    public HttpStatus getStatus() {
        return code;
    }

    @Override
    public String getCode() {
        return serviceStatus.getServiceStatus();
    }

    @Override
    public String getMessage() {
        return message;
    }
}
