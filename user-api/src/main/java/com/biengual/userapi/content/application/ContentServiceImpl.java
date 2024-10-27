package com.biengual.userapi.content.application;

import com.biengual.userapi.content.domain.*;
import com.biengual.userapi.content.presentation.ContentResponseDto;
import com.biengual.userapi.message.error.exception.CommonException;
import com.biengual.userapi.util.PaginationDto;
import com.biengual.userapi.util.PaginationInfo;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.biengual.userapi.message.error.code.ContentErrorCode.CONTENT_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class ContentServiceImpl implements ContentService {
    private final ContentReader contentReader;
    private final ContentStore contentStore;

	private final ContentRepository contentRepository;
	private final ContentScriptRepository contentScriptRepository;

	// 검색 조건에 맞는 컨텐츠 프리뷰 페이지 조회
	@Override
	@Transactional(readOnly = true)
	public PaginationInfo<ContentInfo.PreviewContent> search(ContentCommand.Search command) {
		return contentReader.findPreviewPageBySearch(command);
	}

	@Override
	@Transactional(readOnly = true)
	public PaginationDto<ContentResponseDto.PreviewRes> getAllContents(
		ContentType contentType, Pageable pageable, Long categoryId
	) {
		Page<ContentEntity> page = contentRepository.findAllByContentTypeAndCategory(contentType, pageable, categoryId);

		List<ContentResponseDto.PreviewRes> contents = page.getContent().stream()
			.map(ContentResponseDto.PreviewRes::of)
			.toList();

		return PaginationDto.from(page, contents);
	}

	// 리딩 컨텐츠 뷰 페이지 조회
	@Override
	@Transactional(readOnly = true)
	public PaginationInfo<ContentInfo.ViewContent> getViewContents(ContentCommand.GetReadingContents command) {
		return contentReader.findReadingViewPage(command);
	}

	// 리스닝 컨텐츠 뷰 페이지 조회
	@Override
	@Transactional(readOnly = true)
	public PaginationInfo<ContentInfo.ViewContent> getViewContents(ContentCommand.GetListeningContents command) {
		return contentReader.findListeningViewPage(command);
	}

	@Override
	@Transactional
	public void createContent(ContentCommand.Create command) {
		contentStore.createContent(command);
	}

	@Override
	@Transactional
	public void updateContent(ContentCommand.Modify command) {
		contentStore.updateContent(command);
	}

	@Override
	@Transactional(readOnly = true)
	public List<ContentResponseDto.PreviewRes> findPreviewContents(
		ContentType contentType, String sortBy, int num
	) {
		return contentRepository.findPreviewContents(contentType, sortBy, num);
	}

	// 스크랩 많은 순 컨텐츠 조회
	@Override
	@Transactional(readOnly = true)
	public ContentInfo.PreviewContents getContentsByScrapCount(Integer size) {
        return ContentInfo.PreviewContents.of(contentReader.findContentsByScrapCount(size));
    }

	@Override
	@Transactional(readOnly = true)
	public List<ContentResponseDto.GetByScrapCount> contentByScrapCount(int num) {
		return contentRepository.contentByScrapCount(num);
	}

	@Override
	@Transactional
	public void deactivateContent(Long contentId) {
		contentStore.deactivateContent(contentId);
	}

	@Override
	@Transactional    // hit 증가 로직 있어서 readOnly 생략
	public ContentResponseDto.DetailRes getScriptsOfContent(Long id) {
		ContentEntity content = contentRepository.findById(id)
			.orElseThrow(() -> new CommonException(CONTENT_NOT_FOUND));

		ContentDocument contentDocument = contentScriptRepository.findById(
			new ObjectId(content.getMongoContentId())
		).orElseThrow(() -> new CommonException(CONTENT_NOT_FOUND));

		contentRepository.updateHit(id); // TODO: 추후 레디스로 바꿀 예정

		return ContentResponseDto.DetailRes.of(content, contentDocument);
	}
}
