package com.biengual.userapi.content.presentation;

import java.util.List;

import org.mapstruct.*;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.biengual.core.domain.document.content.script.Script;
import com.biengual.core.domain.document.content.script.YoutubeScript;
import com.biengual.core.domain.entity.content.ContentEntity;
import com.biengual.core.enums.ContentType;
import com.biengual.core.util.PaginationInfo;
import com.biengual.userapi.content.domain.ContentCommand;
import com.biengual.userapi.content.domain.ContentInfo;
import com.biengual.userapi.oauth2.info.OAuth2UserPrincipal;

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
	@Mapping(target = "userId", source = "principal.id")
	ContentCommand.Search doSearch(
		Integer page, Integer size, Sort.Direction direction, String sort, String keyword, OAuth2UserPrincipal principal
	);

	@Mapping(target = "pageable", expression = "java(toPageable(page, size, direction, sort))")
	@Mapping(target = "contentType", constant = "READING")
	@Mapping(target = "userId", source = "principal.id")
	ContentCommand.GetReadingView doGetReadingView(
		Integer page, Integer size, Sort.Direction direction, String sort, Long categoryId, OAuth2UserPrincipal principal
	);

	@Mapping(target = "pageable", expression = "java(toPageable(page, size, direction, sort))")
	@Mapping(target = "contentType", constant = "READING")
	ContentCommand.GetAdminReadingView doGetAdminReadingView(
		Integer page, Integer size, Sort.Direction direction, String sort, Long categoryId
	);

	@Mapping(target = "pageable", expression = "java(toPageable(page, size, direction, sort))")
	@Mapping(target = "contentType", constant = "LISTENING")
	@Mapping(target = "userId", source = "principal.id")
	ContentCommand.GetListeningView doGetListeningView(
		Integer page, Integer size, Sort.Direction direction, String sort, Long categoryId, OAuth2UserPrincipal principal
	);

	@Mapping(target = "pageable", expression = "java(toPageable(page, size, direction, sort))")
	@Mapping(target = "contentType", constant = "LISTENING")
	ContentCommand.GetAdminListeningView doGetAdminListeningView(
		Integer page, Integer size, Sort.Direction direction, String sort, Long categoryId
	);

	@Mapping(target = "contentType", constant = "READING")
	@Mapping(target = "userId", source = "principal.id")
	ContentCommand.GetReadingPreview doGetReadingPreview(Integer size, String sort, OAuth2UserPrincipal principal);

	@Mapping(target = "contentType", constant = "LISTENING")
	@Mapping(target = "userId", source = "principal.id")
	ContentCommand.GetListeningPreview doGetListeningPreview(Integer size, String sort, OAuth2UserPrincipal principal);

	@Mapping(target = "userId", source = "principal.id")
	ContentCommand.GetDetail doGetDetail(Long contentId, OAuth2UserPrincipal principal);

	@Mapping(target = "userId", source = "principal.id")
	ContentCommand.GetScrapPreview doGetScrapPreview(Integer size, OAuth2UserPrincipal principal);

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

	@Mapping(target = "duration", source = "videoDurationInSeconds", qualifiedByName = "toDurationFormat")
	ContentResponseDto.PreviewContent ofPreviewContent(ContentInfo.PreviewContent previewContent);

	@Mapping(target = "duration", source = "videoDurationInSeconds", qualifiedByName = "toDurationFormat")
	ContentResponseDto.ViewContent ofViewContent(ContentInfo.ViewContent viewContent);

	@Mapping(target = "duration", source = "videoDurationInSeconds", qualifiedByName = "toDurationFormat")
	ContentResponseDto.DetailRes ofDetailRes(ContentInfo.Detail detail);

	@Mapping(target = "startTimeInSecond", source = "userScript.script", qualifiedByName = "mapStartTimeInSecond")
	@Mapping(target = "durationInSecond", source = "userScript.script", qualifiedByName = "mapDurationInSecond")
	@Mapping(target = "enScript", source = "userScript.script.enScript")
	@Mapping(target = "koScript", source = "userScript.script.koScript")
	ContentResponseDto.UserScript ofUserScript(ContentInfo.UserScript userScript);

	@Mapping(target = "pageNumber", source = "pageNumber", qualifiedByName = "toResPageNumber")
	ContentResponseDto.AdminListRes ofAdminListRes(PaginationInfo<ContentInfo.Admin> adminPaginationInfo);

	// Entity <-> Info, Info <-> Info
	@Mapping(target = "contentId", source = "content.id")
	@Mapping(target = "videoUrl", source = "content", qualifiedByName = "toVideoUrl")
	@Mapping(target = "category", source = "content.category.name")
	@Mapping(target = "isScrapped", constant = "false")
	@Mapping(target = "learningRate", ignore = true)
	@Mapping(target = "scriptList", source = "userScripts")
	@Mapping(target = "videoDurationInSeconds", source = "content.videoDuration")
	ContentInfo.Detail buildDetail(ContentEntity content, List<ContentInfo.UserScript> userScripts);

	@Mapping(target = "contentId", source = "content.id")
	@Mapping(target = "videoUrl", source = "content", qualifiedByName = "toVideoUrl")
	@Mapping(target = "category", source = "content.category.name")
	@Mapping(target = "isScrapped", source = "isScrapped")
	@Mapping(target = "learningRate", source = "learningRate")
	@Mapping(target = "scriptList", source = "userScripts")
	@Mapping(target = "videoDurationInSeconds", source = "content.videoDuration")
	ContentInfo.Detail buildDetail(
		ContentEntity content, Boolean isScrapped, Integer learningRate, List<ContentInfo.UserScript> userScripts
	);

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

	@Named("mapStartTimeInSecond")
	default Double mapStartTimeInSecond(Script script) {
		if (script instanceof YoutubeScript) {
			return ((YoutubeScript) script).getStartTimeInSecond();
		}
		return null;
	}

	@Named("mapDurationInSecond")
	default Double mapDurationInSecond(Script script) {
		if (script instanceof YoutubeScript) {
			return ((YoutubeScript) script).getDurationInSecond();
		}
		return null;
	}

	@Named("toDurationFormat")
	default String toVideoDurationFormat(Integer videoDurationInSeconds) {
		if (videoDurationInSeconds != null) {
			int minutes = videoDurationInSeconds / 60;
			int seconds = videoDurationInSeconds % 60;
			return String.format("%d:%02d", minutes, seconds);
		}
		return null;
	}
}
