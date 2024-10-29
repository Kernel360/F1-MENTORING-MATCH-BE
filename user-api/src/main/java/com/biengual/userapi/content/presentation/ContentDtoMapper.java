package com.biengual.userapi.content.presentation;

import java.util.List;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.biengual.core.domain.document.content.script.Script;
import com.biengual.core.domain.entity.content.ContentEntity;
import com.biengual.core.enums.ContentType;
import com.biengual.core.util.PaginationInfo;
import com.biengual.userapi.content.domain.ContentCommand;
import com.biengual.userapi.content.domain.ContentInfo;

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

	@Mapping(target = "pageable", expression = "java(toPageable(page, size, direction, sort))")
	ContentCommand.Search doSearch(Integer page, Integer size, Sort.Direction direction, String sort, String keyword);

	@Mapping(target = "pageable", expression = "java(toPageable(page, size, direction, sort))")
	@Mapping(target = "contentType", constant = "READING")
	ContentCommand.GetReadingView doGetReadingView(
		Integer page, Integer size, Sort.Direction direction, String sort, Long categoryId
	);

	@Mapping(target = "pageable", expression = "java(toPageable(page, size, direction, sort))")
	@Mapping(target = "contentType", constant = "LISTENING")
	ContentCommand.GetListeningView doGetListeningView(
		Integer page, Integer size, Sort.Direction direction, String sort, Long categoryId
	);

	@Mapping(target = "contentType", constant = "READING")
	ContentCommand.GetReadingPreview doGetReadingPreview(Integer size, String sort);

	@Mapping(target = "contentType", constant = "LISTENING")
	ContentCommand.GetListeningPreview doGetListeningPreview(Integer size, String sort);

	// Response <- Info
    @Mapping(target = "contentByScrapCount", source = "previewContents")
    ContentResponseDto.ScrapPreviewContentsRes ofScrapPreviewContentsRes(ContentInfo.PreviewContents previewContents);

	@Mapping(target = "pageNumber", source = "pageNumber", qualifiedByName = "toResPageNumber")
	ContentResponseDto.SearchPreviewContentsRes ofSearchPreviewContentsRes(
		PaginationInfo<ContentInfo.PreviewContent> searchPreview
	);

	@Mapping(target = "pageNumber", source = "pageNumber", qualifiedByName = "toResPageNumber")
	ContentResponseDto.ReadingViewContentsRes ofReadingViewContentsRes(
		PaginationInfo<ContentInfo.ViewContent> readingView
	);

	@Mapping(target = "pageNumber", source = "pageNumber", qualifiedByName = "toResPageNumber")
	ContentResponseDto.ListeningViewContentsRes ofListeningViewContentsRes(
		PaginationInfo<ContentInfo.ViewContent> readingView
	);

	@Mapping(target = "readingPreview", source = "previewContents")
	ContentResponseDto.ReadingPreviewContentsRes ofReadingPreviewContentsRes(
		ContentInfo.PreviewContents readingPreview
	);

    @Mapping(target = "listeningPreview", source = "previewContents")
    ContentResponseDto.ListeningPreviewContentsRes ofListeningPreviewContentsRes(
        ContentInfo.PreviewContents listeningPreview
    );

	ContentResponseDto.DetailRes ofDetailRes(ContentInfo.Detail detail);

	@Mapping(target = "pageNumber", source = "pageNumber", qualifiedByName = "toResPageNumber")
	ContentResponseDto.AdminListRes ofAdminListRes(PaginationInfo<ContentInfo.Admin> adminPaginationInfo);

	// Entity <-> Info, Info <-> Info
	@Mapping(target = "contentId", source = "content.id")
	@Mapping(target = "videoUrl", source = "content", qualifiedByName = "toVideoUrl")
	@Mapping(target = "category", source = "content.category.name")
	@Mapping(target = "scriptList", source = "scripts")
	ContentInfo.Detail buildDetail(ContentEntity content, List<Script> scripts);

	// Internal Method =================================================================================================

	@Named("toPageable")
	default Pageable toPageable(Integer page, Integer size, Sort.Direction direction, String sort) {
		return PageRequest.of(page - 1, size, direction, sort);
	}

	@Named("toResPageNumber")
	default Integer toResPageNumber(Integer infoPageNumber) {
		return infoPageNumber + 1;
	}

	@Named("toVideoUrl")
	default String toVideoUrl(ContentEntity content) {
		return content.getContentType().equals(ContentType.LISTENING) ? content.getUrl() : null;
	}
}
