package com.biengual.userapi.content.infrastructure;

import static com.biengual.userapi.core.message.error.code.ContentErrorCode.*;

import java.util.List;

import org.springframework.data.domain.Page;

import com.biengual.userapi.core.annotation.DataProvider;
import com.biengual.userapi.content.domain.ContentCommand;
import com.biengual.userapi.content.domain.ContentCustomRepository;
import com.biengual.userapi.core.entity.content.ContentEntity;
import com.biengual.userapi.content.domain.ContentInfo;
import com.biengual.userapi.content.domain.ContentReader;
import com.biengual.userapi.content.domain.ContentRepository;
import com.biengual.userapi.core.common.enums.ContentStatus;
import com.biengual.userapi.core.message.error.exception.CommonException;
import com.biengual.userapi.core.common.util.PaginationInfo;

import lombok.RequiredArgsConstructor;

@DataProvider
@RequiredArgsConstructor
public class ContentReaderImpl implements ContentReader {
	private final ContentRepository contentRepository;
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

	// 리딩 컨텐츠 프리뷰 페이지 조회
	@Override
	public PaginationInfo<ContentInfo.ViewContent> findReadingViewPage(ContentCommand.GetReadingView command) {
		Page<ContentInfo.ViewContent> page = contentCustomRepository.findViewPageByContentTypeAndCategoryId(
			command.pageable(), command.contentType(), command.categoryId()
		);

		return PaginationInfo.from(page);
	}

	// 리스닝 컨텐츠 프리뷰 페이지 조회
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

	// 리스닝 컨텐츠 프리뷰 조회
	@Override
	public List<ContentInfo.PreviewContent> findListeningPreview(ContentCommand.GetListeningPreview command) {
		return contentCustomRepository.findPreviewBySizeAndSortAndContentType(
			command.size(), command.sort(), command.contentType()
		);
	}

	// script를 제외한 컨텐츠 디테일 조회
	@Override
	public ContentEntity findActiveContent(Long contentId) {
		ContentEntity content = contentRepository.findById(contentId)
			.orElseThrow(() -> new CommonException(CONTENT_NOT_FOUND));

		if (!content.getContentStatus().equals(ContentStatus.ACTIVATED)) {
			throw new CommonException(CONTENT_IS_DEACTIVATED);
		}

		return content;
	}

	// 어드민 페이지 리딩 컨텐츠 조회 - DEACTIVATED 포함
	@Override
	public PaginationInfo<ContentInfo.Admin> findReadingAdmin(ContentCommand.GetReadingView command) {
		Page<ContentInfo.Admin> page = contentCustomRepository.findContentDetailForAdmin(
			command.pageable(), command.contentType(), command.categoryId()
		);
		return PaginationInfo.from(page);
	}

	// 어드민 페이지 리스닝 컨텐츠 조회 - DEACTIVATED 포함
	@Override
	public PaginationInfo<ContentInfo.Admin> findListeningAdmin(ContentCommand.GetListeningView command) {
		Page<ContentInfo.Admin> page = contentCustomRepository.findContentDetailForAdmin(
			command.pageable(), command.contentType(), command.categoryId()
		);
		return PaginationInfo.from(page);

	}
}
