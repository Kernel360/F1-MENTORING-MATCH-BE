package com.biengual.userapi.content.domain;

import com.biengual.userapi.core.common.util.PaginationInfo;
import com.biengual.userapi.core.entity.content.ContentEntity;

import java.util.List;

/**
 * Content 도메인의 DataProvider 계층의 인터페이스
 *
 * @author 문찬욱
 */
public interface ContentReader {
    List<ContentInfo.PreviewContent> findContentsByScrapCount(Integer size);

    PaginationInfo<ContentInfo.PreviewContent> findPreviewPageBySearch(ContentCommand.Search command);

    PaginationInfo<ContentInfo.ViewContent> findReadingViewPage(ContentCommand.GetReadingView command);

    PaginationInfo<ContentInfo.ViewContent> findListeningViewPage(ContentCommand.GetListeningView command);

    List<ContentInfo.PreviewContent> findReadingPreview(ContentCommand.GetReadingPreview command);

    List<ContentInfo.PreviewContent> findListeningPreview(ContentCommand.GetListeningPreview command);

    ContentEntity findActiveContent(Long contentId);

    PaginationInfo<ContentInfo.Admin> findReadingAdmin(ContentCommand.GetReadingView command);

    PaginationInfo<ContentInfo.Admin> findListeningAdmin(ContentCommand.GetListeningView command);
}
