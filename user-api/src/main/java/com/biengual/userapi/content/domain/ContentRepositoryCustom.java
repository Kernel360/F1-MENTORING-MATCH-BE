package com.biengual.userapi.content.domain;

import com.biengual.userapi.content.presentation.ContentRequestDto;
import com.biengual.userapi.content.presentation.ContentResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ContentRepositoryCustom {

	Page<ContentEntity> findAllBySearchCondition(
		ContentRequestDto.SearchReq searchDto, Pageable pageable
	);

	List<ContentResponseDto.PreviewRes> findPreviewContents(
		ContentType contentType, String sortBy, int num
	);

	int updateHit(Long contentId);

	Page<ContentEntity> findAllByContentTypeAndCategory(ContentType contentType, Pageable pageable, Long categoryId);

	String findTitleById(Long contentId);

	String findMongoIdByContentId(Long contentId);

	List<ContentResponseDto.GetByScrapCount> contentByScrapCount(int num);

	ContentType findContentTypeById(Long scriptIndex);

	boolean existsByUrl(String url);
}
