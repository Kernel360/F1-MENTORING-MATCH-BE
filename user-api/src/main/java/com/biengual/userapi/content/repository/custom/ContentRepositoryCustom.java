package com.biengual.userapi.content.repository.custom;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.biengual.userapi.content.presentation.ContentRequestDto;
import com.biengual.userapi.content.presentation.ContentResponseDto;
import com.biengual.userapi.content.domain.ContentEntity;
import com.biengual.userapi.content.domain.enums.ContentType;

public interface ContentRepositoryCustom {

	Page<ContentEntity> findAllBySearchCondition(
		ContentRequestDto.ContentSearchDto searchDto, Pageable pageable
	);

	List<ContentResponseDto.ContentPreviewResponseDto> findPreviewContents(
		ContentType contentType, String sortBy, int num
	);

	int updateHit(Long contentId);

	Page<ContentEntity> findAllByContentTypeAndCategory(ContentType contentType, Pageable pageable, Long categoryId);

	String findTitleById(Long contentId);

	String findMongoIdByContentId(Long contentId);

	List<ContentResponseDto.ContentByScrapCountDto> contentByScrapCount(int num);

	ContentType findContentTypeById(Long scriptIndex);

	boolean existsByUrl(String url);
}
