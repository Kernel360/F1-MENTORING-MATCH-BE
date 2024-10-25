package com.biengual.userapi.content.application;

import com.biengual.userapi.content.domain.ContentInfo;
import com.biengual.userapi.content.domain.dto.ContentRequestDto;
import com.biengual.userapi.content.domain.dto.ContentResponseDto;
import com.biengual.userapi.content.domain.enums.ContentType;
import com.biengual.userapi.util.PaginationDto;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;

import java.util.List;

/**
 * Content 도메인의 Service 계층의 인터페이스
 *
 * @author 김영래
 */
public interface ContentService {
	PaginationDto<ContentResponseDto.ContentPreviewResponseDto> search(
		ContentRequestDto.ContentSearchDto searchDto, Pageable pageable
	);

	PaginationDto<ContentResponseDto.ContentPreviewResponseDto> getAllContents(
		ContentType contentType, Pageable pageable, Long categoryId
	);

	ContentResponseDto.ContentDetailResponseDto getScriptsOfContent(Long id);

	ContentResponseDto.ContentCreateResponseDto createContent(
		Authentication authentication, ContentRequestDto.ContentCreateRequestDto contentCreateRequestDto
	) throws Exception;

	ContentResponseDto.ContentUpdateResponseDto updateContent(
		Long id,
		ContentRequestDto.ContentUpdateRequestDto contentUpdateRequest
	);

	ContentResponseDto.ContentUpdateResponseDto deactivateContent(Long id);

	List<ContentResponseDto.ContentPreviewResponseDto> findPreviewContents(
		ContentType contentType, String sortBy, int num
	);

	ContentInfo.PreviewContents getContentsByScrapCount(Integer size);
}
