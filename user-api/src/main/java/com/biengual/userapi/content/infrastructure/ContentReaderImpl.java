package com.biengual.userapi.content.infrastructure;

import com.biengual.userapi.annotation.DataProvider;
import com.biengual.userapi.content.domain.ContentCommand;
import com.biengual.userapi.content.domain.ContentInfo;
import com.biengual.userapi.content.domain.ContentReader;
import com.biengual.userapi.content.repository.ContentCustomRepository;
import com.biengual.userapi.util.PaginationInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.List;

@DataProvider
@RequiredArgsConstructor
public class ContentReaderImpl implements ContentReader {
    private final ContentCustomRepository contentCustomRepository;

    // 스크랩 많은 순 컨텐츠 프리뷰 조회
    @Override
    public List<ContentInfo.PreviewContent> findContentsByScrapCount(Integer size) {
        return contentCustomRepository.findContentsByScrapCount(size);
    }

    // 검색 조건에 맞는 컨텐츠 프리뷰 페이지 조회
    @Override
    public PaginationInfo<ContentInfo.PreviewContent> findPreviewPageBySearch(ContentCommand.Search command) {
        Page<ContentInfo.PreviewContent> page =
            contentCustomRepository.findPreviewPageBySearch(command.pageable(), command.keyword());

        return PaginationInfo.from(page);
    }

    // 리딩 컨텐츠 뷰 페이지 조회
    @Override
    public PaginationInfo<ContentInfo.ViewContent> findReadingViewPage(ContentCommand.GetReadingView command) {
        Page<ContentInfo.ViewContent> page = contentCustomRepository.findViewPageByContentTypeAndCategoryId(
                command.pageable(), command.contentType(), command.categoryId()
            );

        return PaginationInfo.from(page);
    }

    // 리스닝 컨텐츠 뷰 페이지 조회
    @Override
    public PaginationInfo<ContentInfo.ViewContent> findListeningViewPage(ContentCommand.GetListeningView command) {
        Page<ContentInfo.ViewContent> page = contentCustomRepository.findViewPageByContentTypeAndCategoryId(
            command.pageable(), command.contentType(), command.categoryId()
        );

        return PaginationInfo.from(page);
    }

    // 리딩 컨텐츠 프리뷰 조회
    @Override
    public List<ContentInfo.PreviewContent> findReadingPreview(ContentCommand.GetReadingPreview command) {
        return contentCustomRepository.findPreviewBySizeAndSortAndContentType(
            command.size(), command.sort(), command.contentType()
        );
    }
}
