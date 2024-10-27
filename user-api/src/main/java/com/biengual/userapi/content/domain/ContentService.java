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

	PaginationInfo<ContentInfo.ViewContent> getViewContents(ContentCommand.GetReadingView command);

	PaginationInfo<ContentInfo.ViewContent> getViewContents(ContentCommand.GetListeningView command);

	ContentInfo.Detail getScriptsOfContent(Long contentId);

	void createContent(ContentCommand.Create command);

	void updateContent(ContentCommand.Modify command);

	void deactivateContent(Long id);

	List<ContentResponseDto.PreviewRes> findPreviewContents(
		ContentType contentType, String sortBy, int num
	);

	ContentInfo.PreviewContents getPreviewContents(ContentCommand.GetReadingPreview command);

	ContentInfo.PreviewContents getPreviewContents(ContentCommand.GetListeningPreview command);

	List<ContentResponseDto.GetByScrapCount> contentByScrapCount(int num);

    ContentInfo.PreviewContents getContentsByScrapCount(Integer size);
}
