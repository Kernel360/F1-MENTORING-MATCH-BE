package com.biengual.userapi.content.domain.dto;

import java.util.List;

import com.biengual.userapi.content.domain.enums.ContentStatus;
import com.biengual.userapi.content.domain.enums.ContentType;
import com.biengual.userapi.script.domain.entity.Script;

public class ContentRequestDto {

	public record ContentCreateRequestDto(
		ContentType contentType,
		String url
	) {
	}

	public record ContentUpdateRequestDto(
		String url,
		String title,
		List<Script> script,
		ContentStatus contentStatus
	) {

	}

	public record ContentSearchDto(
		String searchWords
	) {
	}
}