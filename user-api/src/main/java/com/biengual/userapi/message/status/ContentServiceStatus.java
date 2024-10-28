package com.biengual.userapi.message.status;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum ContentServiceStatus implements ServiceStatus {
	// success
	CONTENT_CREATE_SUCCESS("U-C-001"),
	CONTENT_VIEW_SUCCESS("U-C-002"),
	CONTENT_MODIFY_SUCCESS("U-C-003"),
	CONTENT_DEACTIVATE_SUCCESS("U-C-004"),
	// error
	CONTENT_NOT_FOUND("U-C-901"),
	CONTENT_SORT_COL_NOT_FOUND("U-C-902"),
	CONTENT_TYPE_NOT_FOUND("U-C-904"),
	CONTENT_IS_DEACTIVATED("U-C-905");

	private final String code;

	@Override
	public String getServiceStatus() {
		return code;
	}
}
