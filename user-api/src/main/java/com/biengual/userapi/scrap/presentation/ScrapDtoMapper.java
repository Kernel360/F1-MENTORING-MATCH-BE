package com.biengual.userapi.scrap.presentation;

import com.biengual.core.enums.ContentStatus;
import org.mapstruct.*;

import com.biengual.core.domain.entity.content.ContentEntity;
import com.biengual.core.domain.entity.scrap.ScrapEntity;
import com.biengual.userapi.oauth2.info.OAuth2UserPrincipal;
import com.biengual.userapi.scrap.domain.ScrapCommand;
import com.biengual.userapi.scrap.domain.ScrapInfo;

/**
 * do~ : Command <- Request
 * of~ : Response <- Info
 * build~ :  Entity <-> Info, Info <-> Info
 * <p>
 * ScrapDto 와 Info, Command 간의 Mapper
 *
 * @author 김영래
 */
@Mapper(
	componentModel = "spring",
	injectionStrategy = InjectionStrategy.CONSTRUCTOR,
	unmappedTargetPolicy = ReportingPolicy.ERROR
)
public interface ScrapDtoMapper {

	// Command <- Request
	@Mapping(target = "userId", source = "principal.id")
	ScrapCommand.GetByContents doGetByContents(Long contentId, OAuth2UserPrincipal principal);

	@Mapping(target = "userId", source = "principal.id")
	ScrapCommand.Create doCreate(Long contentId, OAuth2UserPrincipal principal);

	@Mapping(target = "userId", source = "principal.id")
	ScrapCommand.Delete doDelete(Long contentId, OAuth2UserPrincipal principal);

	// Response <- Info
	ScrapResponseDto.ViewListRes ofViewRes(ScrapInfo.ViewInfo allScraps);

	@Mapping(target = "duration", source = "videoDurationInSeconds", qualifiedByName = "toDurationFormat")
	@Mapping(target = "isActive", source = "contentStatus", qualifiedByName = "toIsActive")
	ScrapResponseDto.View ofView(ScrapInfo.View view);

	// Entity <-> Info, Info <-> Info
	@Mapping(target = "scrapId", source = "id")
	@Mapping(target = "contentId", source = "content.id")
	@Mapping(target = "title", source = "content.title")
	@Mapping(target = "contentType", source = "content.contentType")
	@Mapping(target = "preScripts", source = "content.preScripts")
	@Mapping(target = "thumbnailUrl", source = "content.thumbnailUrl")
	@Mapping(target = "category", source = "content.category.name")
	@Mapping(target = "videoDurationInSeconds", source = "content.videoDuration")
	@Mapping(target = "contentStatus", source = "content.contentStatus")
	ScrapInfo.View buildView(ScrapEntity scrap);

	@Mapping(target = "content", source = "content")
	ScrapEntity buildEntity(Long userId, ContentEntity content);

	// Internal Method =================================================================================================

	@Named("toIsActive")
	default Boolean toIsActive(ContentStatus contentStatus) {
		return contentStatus == ContentStatus.ACTIVATED;
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
