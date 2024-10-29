package com.biengual.userapi.content.presentation;

import com.biengual.userapi.core.common.enums.ContentType;

public class ContentRequestDto {

	public record CreateReq(
		ContentType contentType,
		String url
	) {
	}
}