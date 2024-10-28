package com.biengual.userapi.content.presentation;

import com.biengual.userapi.content.domain.ContentCommand;
import com.biengual.userapi.content.domain.ContentEntity;
import com.biengual.userapi.content.domain.ContentInfo;
import com.biengual.userapi.content.domain.ContentType;
import com.biengual.userapi.script.domain.entity.Script;
import com.biengual.userapi.util.PaginationInfo;
import org.mapstruct.*;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;

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

	ContentResponseDto.SearchPreviewContentsRes ofSearchPreviewContentsRes(
		PaginationInfo<ContentInfo.PreviewContent> searchPreview
	);

	ContentResponseDto.ReadingViewContentsRes ofReadingViewContentsRes(
		PaginationInfo<ContentInfo.ViewContent> readingView
	);

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

	// Entity <-> Info, Info <-> Info
	@Mapping(target = "contentId", source = "content.id")
	@Mapping(target = "videoUrl", source = "content", qualifiedByName = "toVideoUrl")
	@Mapping(target = "category", source = "content.category.name")
	@Mapping(target = "scriptList", source = "scripts")
	ContentInfo.Detail buildDetail(ContentEntity content, List<Script> scripts);

	// Internal Method =================================================================================================

	@Named("toPageable")
	default Pageable toPageable(Integer page, Integer size, Sort.Direction direction, String sort) {
		return PageRequest.of(page, size, direction,sort);
	}

	@Named("toVideoUrl")
	default String toVideoUrl(ContentEntity content) {
		return content.getContentType().equals(ContentType.LISTENING) ? content.getUrl() : null;
	}
}
