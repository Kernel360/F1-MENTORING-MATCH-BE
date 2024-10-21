package com.biengual.userapi.message.status;

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
	;

	private final String code;

	@Override
	public String getServiceStatus() {
		return code;
	}
}
