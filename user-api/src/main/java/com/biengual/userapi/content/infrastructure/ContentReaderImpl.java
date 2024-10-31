package com.biengual.userapi.content.infrastructure;

import static com.biengual.core.response.error.code.ContentErrorCode.*;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;

import com.biengual.core.annotation.DataProvider;
import com.biengual.core.domain.document.content.ContentDocument;
import com.biengual.core.domain.document.content.script.Script;
import com.biengual.core.domain.entity.bookmark.BookmarkEntity;
import com.biengual.core.domain.entity.content.ContentEntity;
import com.biengual.core.enums.ContentStatus;
import com.biengual.core.response.error.exception.CommonException;
import com.biengual.core.util.PaginationInfo;
import com.biengual.userapi.bookmark.domain.BookmarkRepository;
import com.biengual.userapi.content.domain.ContentCommand;
import com.biengual.userapi.content.domain.ContentCustomRepository;
import com.biengual.userapi.content.domain.ContentDocumentRepository;
import com.biengual.userapi.content.domain.ContentInfo;
import com.biengual.userapi.content.domain.ContentReader;
import com.biengual.userapi.content.domain.ContentRepository;
import com.biengual.userapi.content.domain.UserContentBookmarks;
import com.biengual.userapi.content.presentation.ContentDtoMapper;

import lombok.RequiredArgsConstructor;

@DataProvider
@RequiredArgsConstructor
public class ContentReaderImpl implements ContentReader {
	private final ContentDtoMapper contentDtoMapper;
	private final ContentRepository contentRepository;
	private final ContentCustomRepository contentCustomRepository;
	private final ContentDocumentRepository contentDocumentRepository;
	private final BookmarkRepository bookmarkRepository;

	// 스크랩 많은 순 컨텐츠 프리뷰 조회
	@Override
	public List<ContentInfo.PreviewContent> findContentsByScrapCount(ContentCommand.GetScrapPreview command) {
		return contentCustomRepository.findContentsByScrapCount(command.size(), command.userId());
	}

	// 검색 조건에 맞는 컨텐츠 프리뷰 페이지 조회
	@Override
	public PaginationInfo<ContentInfo.PreviewContent> findPreviewPageBySearch(ContentCommand.Search command) {
		Page<ContentInfo.PreviewContent> page =
			contentCustomRepository.findPreviewPageBySearch(command.pageable(), command.keyword(), command.userId());

		return PaginationInfo.from(page);
	}

	// 리딩 컨텐츠 프리뷰 페이지 조회
	@Override
	public PaginationInfo<ContentInfo.ViewContent> findReadingViewPage(ContentCommand.GetReadingView command) {
		Page<ContentInfo.ViewContent> page = contentCustomRepository.findViewPageByContentTypeAndCategoryId(
			command.pageable(), command.contentType(), command.categoryId(), command.userId()
		);

		return PaginationInfo.from(page);
	}

	// 리스닝 컨텐츠 프리뷰 페이지 조회
	@Override
	public PaginationInfo<ContentInfo.ViewContent> findListeningViewPage(ContentCommand.GetListeningView command) {
		Page<ContentInfo.ViewContent> page = contentCustomRepository.findViewPageByContentTypeAndCategoryId(
			command.pageable(), command.contentType(), command.categoryId(), command.userId()
		);

		return PaginationInfo.from(page);
	}

	// 리딩 컨텐츠 프리뷰 조회
	@Override
	public List<ContentInfo.PreviewContent> findReadingPreview(ContentCommand.GetReadingPreview command) {
		return contentCustomRepository.findPreviewBySizeAndSortAndContentType(
			command.size(), command.sort(), command.contentType(), command.userId()
		);
	}

	// 리스닝 컨텐츠 프리뷰 조회
	@Override
	public List<ContentInfo.PreviewContent> findListeningPreview(ContentCommand.GetListeningPreview command) {
		return contentCustomRepository.findPreviewBySizeAndSortAndContentType(
			command.size(), command.sort(), command.contentType(), command.userId()
		);
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

	// TODO: 로직 개선해볼 것
	// 로그인 여부에 따른 컨텐츠 디테일 조회
	@Override
	public ContentInfo.Detail findActiveContentWithScripts(ContentCommand.GetDetail command) {
		ContentEntity content = contentRepository.findById(command.contentId())
			.orElseThrow(() -> new CommonException(CONTENT_NOT_FOUND));

		if (!content.getContentStatus().equals(ContentStatus.ACTIVATED)) {
			throw new CommonException(CONTENT_IS_DEACTIVATED);
		}

		ContentDocument contentDocument =
			contentDocumentRepository.findContentDocumentById(new ObjectId(content.getMongoContentId()))
				.orElseThrow(() -> new CommonException(CONTENT_NOT_FOUND));

		List<Script> scripts = contentDocument.getScripts();

		if (command.userId() != null) {
			List<BookmarkEntity> bookmarks =
				bookmarkRepository.findAllByUserIdAndScriptIndex(command.userId(), command.contentId());
			UserContentBookmarks userContentBookmarks = new UserContentBookmarks(bookmarks);
			List<ContentInfo.UserScript> userScripts = userContentBookmarks.getUserScripts(scripts);

			return contentDtoMapper.buildDetail(content, userScripts);
		}

		List<ContentInfo.UserScript> guestScripts = scripts.stream()
			.map(ContentInfo.UserScript::of)
			.toList();

		return contentDtoMapper.buildDetail(content, guestScripts);
	}
}
