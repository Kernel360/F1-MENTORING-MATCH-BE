package com.biengual.userapi.content.application;

import static com.biengual.userapi.message.error.code.CategoryErrorCode.*;
import static com.biengual.userapi.message.error.code.ContentErrorCode.*;
import static com.biengual.userapi.message.error.code.CrawlingErrorCode.*;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.biengual.userapi.category.domain.CategoryEntity;
import com.biengual.userapi.category.repository.CategoryRepository;
import com.biengual.userapi.content.domain.dto.ContentRequestDto;
import com.biengual.userapi.content.domain.dto.ContentResponseDto;
import com.biengual.userapi.content.domain.entity.ContentDocument;
import com.biengual.userapi.content.domain.entity.ContentEntity;
import com.biengual.userapi.content.domain.enums.ContentStatus;
import com.biengual.userapi.content.domain.enums.ContentType;
import com.biengual.userapi.content.repository.ContentRepository;
import com.biengual.userapi.content.repository.ContentScriptRepository;
import com.biengual.userapi.crawling.domain.dto.CrawlingResponseDto;
import com.biengual.userapi.crawling.service.CrawlingServiceImpl;
import com.biengual.userapi.message.error.exception.CommonException;
import com.biengual.userapi.util.PaginationDto;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ContentServiceImpl implements ContentService {

	private final ContentRepository contentRepository;
	private final ContentScriptRepository contentScriptRepository;
	private final CrawlingServiceImpl crawlingService;
	private final CategoryRepository categoryRepository;

	@Override
	@Transactional(readOnly = true)
	public PaginationDto<ContentResponseDto.ContentPreviewResponseDto> search(
		ContentRequestDto.ContentSearchDto searchDto, Pageable pageable
	) {
		if (searchDto.searchWords().isBlank()) {
			throw new CommonException(CONTENT_SEARCH_WORD_NOT_FOUND);
		}
		Page<ContentEntity> page = contentRepository.findAllBySearchCondition(searchDto, pageable);
		List<ContentResponseDto.ContentPreviewResponseDto> contents
			= page.getContent().stream()
			.map(ContentResponseDto.ContentPreviewResponseDto::of)
			.toList();

		return PaginationDto.from(page, contents);
	}

	@Override
	@Transactional(readOnly = true)
	public PaginationDto<ContentResponseDto.ContentPreviewResponseDto> getAllContents(
		ContentType contentType, Pageable pageable, Long categoryId
	) {
		Page<ContentEntity> page = contentRepository.findAllByContentTypeAndCategory(contentType, pageable, categoryId);

		List<ContentResponseDto.ContentPreviewResponseDto> contents = page.getContent().stream()
			.map(ContentResponseDto.ContentPreviewResponseDto::of)
			.toList();

		return PaginationDto.from(page, contents);
	}

	@Override
	@Transactional    // 다른 네트워크를 사용하는데 transaction 이 맞나?
	public ContentResponseDto.ContentCreateResponseDto createContent(
		Authentication authentication,
		ContentRequestDto.ContentCreateRequestDto contentCreateRequestDto
	) throws Exception {
		CrawlingResponseDto.CrawlingContentResponseDto crawlingContentResponseDto = null;

		verifyCrawling(contentCreateRequestDto.url());

		if (contentCreateRequestDto.contentType().equals(ContentType.LISTENING)) {
			crawlingContentResponseDto = crawlingService.getYoutubeInfo(
				contentCreateRequestDto.url(), String.valueOf(authentication.getCredentials())
			);
		}
		if (contentCreateRequestDto.contentType().equals(ContentType.READING)) {
			crawlingContentResponseDto = crawlingService.getCNNInfo(
				contentCreateRequestDto.url(), String.valueOf(authentication.getCredentials())
			);
		}

		ContentDocument contentDocument = crawlingContentResponseDto.toDocument();
		contentScriptRepository.save(contentDocument);

		CategoryEntity category = getCategoryEntity(crawlingContentResponseDto);

		ContentEntity content = crawlingContentResponseDto.toEntity(
			contentDocument.getId(), contentCreateRequestDto.contentType(), category
		);

		contentRepository.save(content);

		return new ContentResponseDto.ContentCreateResponseDto(contentDocument.getId().toString(), content.getId());
	}

	@Override
	@Transactional
	public ContentResponseDto.ContentUpdateResponseDto updateContent(
		Long id, ContentRequestDto.ContentUpdateRequestDto contentUpdateRequest
	) {
		ContentEntity content = contentRepository.findById(id)
			.orElseThrow(() -> new CommonException(CONTENT_NOT_FOUND));
		content.update(contentUpdateRequest);
		contentRepository.save(content);

		ContentDocument contentDocument
			= contentScriptRepository.findContentDocumentById(new ObjectId(content.getMongoContentId()))
			.orElseThrow(() -> new CommonException(CONTENT_NOT_FOUND));

		contentDocument.updateScript(contentUpdateRequest.script());
		contentScriptRepository.save(contentDocument);

		return new ContentResponseDto.ContentUpdateResponseDto(content.getId());
	}

	@Override
	@Transactional(readOnly = true)
	public List<ContentResponseDto.ContentPreviewResponseDto> findPreviewContents(
		ContentType contentType, String sortBy, int num
	) {
		return contentRepository.findPreviewContents(contentType, sortBy, num);
	}

	@Override
	@Transactional(readOnly = true)
	public List<ContentResponseDto.ContentByScrapCountDto> contentByScrapCount(int num) {
		return contentRepository.contentByScrapCount(num);
	}

	@Override
	@Transactional
	public ContentResponseDto.ContentUpdateResponseDto deactivateContent(Long id) {
		ContentEntity content = contentRepository.findById(id)
			.orElseThrow(() -> new CommonException(CONTENT_NOT_FOUND));
		content.updateStatus(ContentStatus.DEACTIVATED);
		contentRepository.save(content);

		return new ContentResponseDto.ContentUpdateResponseDto(content.getId());
	}

	@Override
	@Transactional    // hit 증가 로직 있어서 readOnly 생략
	public ContentResponseDto.ContentDetailResponseDto getScriptsOfContent(Long id) {
		ContentEntity content = contentRepository.findById(id)
			.orElseThrow(() -> new CommonException(CONTENT_NOT_FOUND));

		ContentDocument contentDocument = contentScriptRepository.findById(
			new ObjectId(content.getMongoContentId())
		).orElseThrow(() -> new CommonException(CONTENT_NOT_FOUND));

		contentRepository.updateHit(id); // TODO: 추후 레디스로 바꿀 예정

		return ContentResponseDto.ContentDetailResponseDto.of(content, contentDocument);
	}

	// Internal Methods=================================================================================================

	private CategoryEntity getCategoryEntity(CrawlingResponseDto.CrawlingContentResponseDto dto) {
		if (categoryRepository.existsByName(dto.category())) {
			return categoryRepository.findByName(dto.category())
				.orElseThrow(() -> new CommonException(CATEGORY_NOT_FOUND));
		}

		return categoryRepository.save(dto.toCategoryEntity());
	}

	private void verifyCrawling(String url) {
		if (contentRepository.existsByUrl(url)) {
			throw new CommonException(CRAWLING_ALREADY_DONE);
		}
	}
}
