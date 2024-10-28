package com.biengual.userapi.content.domain;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.biengual.userapi.content.presentation.ContentRequestDto;
import com.biengual.userapi.content.presentation.ContentResponseDto;
import com.biengual.userapi.util.PaginationDto;

public interface ContentService {
	PaginationDto<ContentResponseDto.PreviewRes> search(
		ContentRequestDto.SearchReq searchDto, Pageable pageable
	);

	PaginationDto<ContentResponseDto.PreviewRes> getAllContents(
		ContentType contentType, Pageable pageable, Long categoryId
	);

	ContentResponseDto.DetailRes getScriptsOfContent(Long id);

	void createContent(ContentCommand.Create command);

	void modifyContentStatus(Long contentId);

	List<ContentResponseDto.PreviewRes> findPreviewContents(
		ContentType contentType, String sortBy, int num
	);

	List<ContentResponseDto.GetByScrapCount> contentByScrapCount(int num);
}
