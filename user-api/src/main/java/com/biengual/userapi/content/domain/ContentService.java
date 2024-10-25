package com.biengual.userapi.content.domain;

import com.biengual.userapi.content.presentation.ContentRequestDto;
import com.biengual.userapi.content.presentation.ContentResponseDto;
import com.biengual.userapi.util.PaginationDto;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Content 도메인의 Service 계층의 인터페이스
 *
 * @author 김영래
 */
public interface ContentService {
	PaginationDto<ContentResponseDto.PreviewRes> search(
		ContentRequestDto.SearchReq searchDto, Pageable pageable
	);

	PaginationDto<ContentResponseDto.PreviewRes> getAllContents(
		ContentType contentType, Pageable pageable, Long categoryId
	);

	ContentResponseDto.DetailRes getScriptsOfContent(Long id);

	void createContent(ContentCommand.Create command);

	void updateContent(ContentCommand.Modify command);

	void deactivateContent(Long id);

	List<ContentResponseDto.PreviewRes> findPreviewContents(
		ContentType contentType, String sortBy, int num
	);

	List<ContentResponseDto.GetByScrapCount> contentByScrapCount(int num);

    ContentInfo.PreviewContents getContentsByScrapCount(Integer size);
}
