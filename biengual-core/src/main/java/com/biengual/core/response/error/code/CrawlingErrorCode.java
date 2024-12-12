package com.biengual.core.response.error.code;

import org.springframework.http.HttpStatus;

import com.biengual.core.response.status.CrawlingServiceStatus;
import com.biengual.core.response.status.ServiceStatus;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum CrawlingErrorCode implements ErrorCode {
	CRAWLING_OUT_OF_BOUNDS(
		HttpStatus.BAD_REQUEST, CrawlingServiceStatus.CRAWLING_OUT_OF_BOUNDS, "유튜브 영상 길이 에러(2분 ~ 10분 만 가능)"
	),
	SELENIUM_RUNTIME_ERROR(
		HttpStatus.NOT_ACCEPTABLE, CrawlingServiceStatus.CRAWLING_SELENIUM_FAILURE, "셀레니움 런타임 에러"
	),
	CRAWLING_TRANSLATE_FAILURE(
		HttpStatus.CONFLICT, CrawlingServiceStatus.CRAWLING_TRANSLATE_FAILURE, "Azure API 번역 에러"
	),
	CRAWLING_JSOUP_FAILURE(
		HttpStatus.NOT_ACCEPTABLE, CrawlingServiceStatus.CRAWLING_JSOUP_FAILURE, "JSOUP 런타임 에러"
	),
	CRAWLING_ALREADY_DONE(
		HttpStatus.BAD_REQUEST, CrawlingServiceStatus.CRAWLING_ALREADY_DONE, "이미 DB에 저장된 크롤링 데이터"
	),
	CRAWLING_JACKSON_ERROR(
		HttpStatus.CONFLICT, CrawlingServiceStatus.CRAWLING_JACKSON_ERROR, "Jackson 매핑 에러"
	),
	CRAWLING_RSS_PARSING_ERROR(
		HttpStatus.CONFLICT, CrawlingServiceStatus.CRAWLING_RSS_PARSING_ERROR, "RSS 파싱 에러"
	),
	CRAWLING_NO_NEW_CONTENT(
		HttpStatus.NOT_FOUND, CrawlingServiceStatus.CRAWLING_NO_NEW_CONTENT, "업데이트 할 컨텐츠 없음"
	)
	;

	private final HttpStatus httpStatus;
	private final ServiceStatus serviceStatus;
	private final String message;

	@Override
	public HttpStatus getStatus() {
		return httpStatus;
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
