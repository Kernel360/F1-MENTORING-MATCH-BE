package com.biengual.userapi.bookmark.presentation;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import com.biengual.userapi.bookmark.domain.BookmarkCommand;
import com.biengual.userapi.bookmark.domain.BookmarkEntity;
import com.biengual.userapi.bookmark.domain.BookmarkInfo;
import com.biengual.userapi.content.domain.enums.ContentType;
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
	BookmarkCommand.GetByContents doGetByContents(BookmarkRequestDto.ViewReq request, OAuth2UserPrincipal principal);

	@Mapping(target = "userId", source = "principal.id")
	BookmarkCommand.Create doCreate(Long contentId, BookmarkRequestDto.CreateReq request,
		OAuth2UserPrincipal principal);

	@Mapping(target = "userId", source = "principal.id")
	BookmarkCommand.Delete doDelete(Long bookmarkId, OAuth2UserPrincipal principal);

	@Mapping(target = "userId", source = "principal.id")
	BookmarkCommand.Update doUpdate(BookmarkRequestDto.UpdateReq request, OAuth2UserPrincipal principal);

	// Response <-> Info
	BookmarkResponseDto.ContentListRes doContentListRes(BookmarkInfo.PositionInfo positionInfos);

	BookmarkResponseDto.MyListRes doMyListRes(BookmarkInfo.MyListInfo myList);

	BookmarkResponseDto.ContentList doContentList(BookmarkInfo.Position position);

	// Entity <-> Info
	@Mapping(target = "bookmarkId", source = "id")
	BookmarkInfo.Position doPosition(BookmarkEntity bookmark);

	@Mapping(target = "bookmarkId", source = "bookmark.id")
	@Mapping(target = "bookmarkDetail", source = "bookmark.detail")
	@Mapping(target = "contentId", source = "bookmark.scriptIndex")
	@Mapping(target = "contentTitle", source = "title")
	BookmarkInfo.MyList doMyList(BookmarkEntity bookmark, ContentType contentType, String title);

}
