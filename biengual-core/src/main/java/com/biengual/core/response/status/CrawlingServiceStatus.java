package com.biengual.core.response.status;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum CrawlingServiceStatus implements ServiceStatus {
	// success

	// error,
	CRAWLING_OUT_OF_BOUNDS("U-CR-901"),
	CRAWLING_SELENIUM_FAILURE("U-CR-902"),
	CRAWLING_TRANSLATE_FAILURE("U-CR-903"),
	CRAWLING_JSOUP_FAILURE("U-CR-904"),
	CRAWLING_ALREADY_DONE("U-CR-905"),
	CRAWLING_JACKSON_ERROR("U-CR-906"),
	CRAWLING_RSS_PARSING_ERROR("U-CR-907"),
	CRAWLING_NO_NEW_CONTENT("U-CR-908")
	;

	private final String code;

	@Override
	public String getServiceStatus() {
		return code;
	}
}
