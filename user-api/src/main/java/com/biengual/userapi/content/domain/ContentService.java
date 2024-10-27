package com.biengual.userapi.content.domain;

import com.biengual.userapi.content.presentation.ContentResponseDto;
import com.biengual.userapi.util.PaginationDto;
import com.biengual.userapi.util.PaginationInfo;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Content 도메인의 Service 계층의 인터페이스
 *
 * @author 김영래
 */
public interface ContentService {
	PaginationInfo<ContentInfo.PreviewContent> search(ContentCommand.Search command);

	PaginationDto<ContentResponseDto.PreviewRes> getAllContents(
		ContentType contentType, Pageable pageable, Long categoryId
	);

	PaginationInfo<ContentInfo.ViewContent> getViewContents(ContentCommand.GetReadingContents command);

	PaginationInfo<ContentInfo.ViewContent> getViewContents(ContentCommand.GetListeningContents command);

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
