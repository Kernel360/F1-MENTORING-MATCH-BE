package com.biengual.userapi.content.presentation;

import com.biengual.core.enums.ContentType;

public class ContentRequestDto {

	public record CreateReq(
		ContentType contentType,
		String url
	) {
	}
}