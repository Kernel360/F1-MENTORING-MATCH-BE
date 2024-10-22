package com.biengual.userapi.message.status;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum BookmarkServiceStatus implements ServiceStatus {
	// success
	BOOKMARK_VIEW_SUCCESS("U-B-001"),
	BOOKMARK_UPDATE_SUCCESS("U-B-002"),
	BOOKMARK_CREATE_SUCCESS("U-B-003"),
	BOOKMARK_DELETE_SUCCESS("U-B-004"),

	// failure,
	BOOKMARK_NOT_FOUND("U-B-901"),
	BOOKMARK_ALREADY_EXISTS("U-B-902")
	;

	private final String code;

	@Override
	public String getServiceStatus() {
		return code;
	}
}
