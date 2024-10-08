package com.echall.platform.message.status;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum BookmarkServiceStatus implements ServiceStatus {
	// success
	BOOKMARK_VIEW_SUCCESS("U-B-001"),
	BOOKMARK_UPDATE_SUCCESS("U-B-002"),
	BOOKMARK_CREATE_SUCCESS("U-B-003"),

	// failure,
	BOOKMARK_NOT_FOUND("U-B-901"),
	BOOKMARK_DESCRIPTION_SAME("U-B-902"),
	BOOKMARK_ALREADY_EXISTS("U-B-903"),
	BOOKMARK_WORD_NEED_SENTENCE("U-B-904")
	;

	private final String code;

	@Override
	public String getServiceStatus() {
		return code;
	}
}
