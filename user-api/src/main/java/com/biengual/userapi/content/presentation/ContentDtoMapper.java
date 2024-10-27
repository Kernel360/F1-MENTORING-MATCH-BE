package com.biengual.userapi.content.presentation;

import com.biengual.userapi.content.domain.ContentCommand;
import com.biengual.userapi.content.domain.ContentInfo;
import com.biengual.userapi.util.PaginationInfo;
import org.mapstruct.*;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

/**
 * do~ : Command <- Request
 * of~ : Response <- Info
 * build~ :  Entity <-> Info, Info <-> Info
 * <p>
 * ContentDto, CrawlingDto 와 Info, Command 간의 Mapper
 *
 * @author 김영래
 */
@Mapper(
	componentModel = "spring",
	injectionStrategy = InjectionStrategy.CONSTRUCTOR,
	unmappedTargetPolicy = ReportingPolicy.ERROR
)
public interface ContentDtoMapper {
	// Command <- Request
	ContentCommand.CrawlingContent doCrawlingContent(ContentRequestDto.CreateReq request);

	@Mapping(target = "id", source = "contentId")
	ContentCommand.Modify doModify(Long contentId, ContentRequestDto.UpdateReq request);

	@Mapping(target = "pageable", expression = "java(toPageable(page, size, direction, sort))")
	ContentCommand.Search doSearch(Integer page, Integer size, Sort.Direction direction, String sort, String keyword);

	@Mapping(target = "pageable", expression = "java(toPageable(page, size, direction, sort))")
	@Mapping(target = "contentType", constant = "READING")
	ContentCommand.GetReadingContents doGetReadingContents(
		Integer page, Integer size, Sort.Direction direction, String sort, Long categoryId
	);

	@Mapping(target = "pageable", expression = "java(toPageable(page, size, direction, sort))")
	@Mapping(target = "contentType", constant = "LISTENING")
	ContentCommand.GetListeningContents doGetListeningContents(
		Integer page, Integer size, Sort.Direction direction, String sort, Long categoryId
	);

	// Response <- Info
    @Mapping(target = "scrapPreview", source = "previewContents")
    ContentResponseDto.ScrapPreviewContentsRes ofScrapPreviewContentsRes(ContentInfo.PreviewContents previewContents);

	@Mapping(target = "searchPreview", source = "contents")
	ContentResponseDto.SearchPreviewContentsRes ofSearchPreviewContentsRes(
		PaginationInfo<ContentInfo.PreviewContent> searchPreview
	);

	@Mapping(target = "readingView", source = "contents")
	ContentResponseDto.ReadingViewContentsRes ofReadingViewContentsRes(
		PaginationInfo<ContentInfo.ViewContent> readingView
	);

	@Mapping(target = "listeningView", source = "contents")
	ContentResponseDto.ListeningViewContentsRes ofListeningViewContentsRes(
		PaginationInfo<ContentInfo.ViewContent> readingView
	);

	// Entity <-> Info, Info <-> Info

	// Internal Method =================================================================================================

	@Named("toPageable")
	default Pageable toPageable(Integer page, Integer size, Sort.Direction direction, String sort) {
		return PageRequest.of(page, size, direction,sort);
	}
}
