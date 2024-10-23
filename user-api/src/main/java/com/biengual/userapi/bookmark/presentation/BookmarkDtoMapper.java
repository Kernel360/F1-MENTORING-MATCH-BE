package com.biengual.userapi.bookmark.presentation;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import com.biengual.userapi.bookmark.domain.BookmarkCommand;
import com.biengual.userapi.bookmark.domain.BookmarkInfo;
import com.biengual.userapi.oauth2.domain.info.OAuth2UserPrincipal;

/**
 * BookmarkDto 와 Info 간의 Mapper
 *
 * @author 김영래
 */
@Mapper(
	componentModel = "spring",
	injectionStrategy = InjectionStrategy.CONSTRUCTOR,
	unmappedTargetPolicy = ReportingPolicy.ERROR
)
public interface BookmarkDtoMapper {

	// Request <-> Info
	@Mapping(target = "userId", source = "principal.id")
	@Mapping(target = "contentId", source = "request.contentId")
	BookmarkCommand.GetByContents of(BookmarkRequestDto.ViewReq request, OAuth2UserPrincipal principal);

	@Mapping(target = "userId", source = "principal.id")
	@Mapping(target = "sentenceIndex", source = "request.sentenceIndex")
	@Mapping(target = "wordIndex", source = "request.wordIndex")
	@Mapping(target = "description", source = "request.description")
	BookmarkCommand.Create of(Long contentId, BookmarkRequestDto.CreateReq request, OAuth2UserPrincipal principal);

	@Mapping(target = "userId", source = "principal.id")
	BookmarkCommand.Delete of(Long bookmarkId, OAuth2UserPrincipal principal);

	@Mapping(target = "userId", source = "principal.id")
	BookmarkCommand.Update of(BookmarkRequestDto.UpdateReq request, OAuth2UserPrincipal principal);

	// Response <-> Info
	BookmarkResponseDto.ContentListRes of(BookmarkInfo.PositionInfo positionInfos);

	BookmarkResponseDto.MyListRes of(BookmarkInfo.MyListInfo myList);

	BookmarkResponseDto.ContentList of(BookmarkInfo.Position position);

}
