package com.biengual.userapi.content.domain;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.biengual.userapi.content.presentation.ContentRequestDto;
import com.biengual.userapi.content.presentation.ContentResponseDto;

public interface ContentRepositoryCustom {

	Page<ContentEntity> findAllBySearchCondition(
		ContentRequestDto.SearchReq searchDto, Pageable pageable
	);

	List<ContentResponseDto.PreviewRes> findPreviewContents(
		ContentType contentType, String sortBy, int num
	);

	int updateHit(Long contentId);

	Page<ContentEntity> findAllByContentTypeAndCategory(ContentType contentType, Pageable pageable, Long categoryId);

	List<ContentResponseDto.GetByScrapCount> contentByScrapCount(int num);

}
