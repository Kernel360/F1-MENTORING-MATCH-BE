package com.biengual.userapi.content.domain;

import com.biengual.core.util.PaginationInfo;

/**
 * Content 도메인의 Service 계층의 인터페이스
 *
 * @author 김영래
 */
public interface ContentService {
	PaginationInfo<ContentInfo.PreviewContent> search(ContentCommand.Search command);

	PaginationInfo<ContentInfo.ViewContent> getViewContents(ContentCommand.GetReadingView command);

	PaginationInfo<ContentInfo.ViewContent> getViewContents(ContentCommand.GetListeningView command);

	ContentInfo.Detail getScriptsOfContent(Long contentId);

	void createContent(ContentCommand.Create command);

	void modifyContentStatus(Long contentId);

	ContentInfo.PreviewContents getPreviewContents(ContentCommand.GetReadingPreview command);

	ContentInfo.PreviewContents getPreviewContents(ContentCommand.GetListeningPreview command);

    ContentInfo.PreviewContents getContentsByScrapCount(Integer size);

	PaginationInfo<ContentInfo.Admin> getAdminView(ContentCommand.GetReadingView command);

	PaginationInfo<ContentInfo.Admin> getAdminView(ContentCommand.GetListeningView command);
}
