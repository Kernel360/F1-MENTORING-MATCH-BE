package com.biengual.userapi.bookmark.presentation;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

import com.biengual.core.domain.entity.bookmark.BookmarkEntity;
import com.biengual.core.enums.ContentStatus;
import com.biengual.userapi.bookmark.domain.BookmarkCommand;
import com.biengual.userapi.bookmark.domain.BookmarkInfo;
import com.biengual.userapi.oauth2.info.OAuth2UserPrincipal;

/**
 * do~ : Command <- Request
 * of~ : Response <- Info
 * build~ :  Entity <-> Info, Info <-> Info
 * <p>
 * BookmarkDto 와 Info, Command 간의 Mapper
 *
 * @author 김영래
 */
@Mapper(
    componentModel = "spring",
    injectionStrategy = InjectionStrategy.CONSTRUCTOR,
    unmappedTargetPolicy = ReportingPolicy.ERROR
)
public interface BookmarkDtoMapper {

    // Command <- Request
    @Mapping(target = "userId", source = "principal.id")
    BookmarkCommand.GetByContents doGetByContents(Long contentId, OAuth2UserPrincipal principal);

    @Mapping(target = "userId", source = "principal.id")
    BookmarkCommand.Create doCreate(Long contentId, BookmarkRequestDto.CreateReq request,
        OAuth2UserPrincipal principal);

    @Mapping(target = "userId", source = "principal.id")
    BookmarkCommand.Delete doDelete(Long bookmarkId, OAuth2UserPrincipal principal);

    @Mapping(target = "userId", source = "principal.id")
    BookmarkCommand.Update doUpdate(
        Long contentId, BookmarkRequestDto.UpdateReq request, OAuth2UserPrincipal principal
    );

    // Response <- Info
    BookmarkResponseDto.ContentListRes ofContentListRes(BookmarkInfo.PositionInfo positionInfos);

    BookmarkResponseDto.MyListRes ofMyListRes(BookmarkInfo.MyListInfo myListInfo);

    @Mapping(target = "isActive", source = "contentStatus", qualifiedByName = "toIsActive")
    BookmarkResponseDto.MyList ofMyList(BookmarkInfo.MyList myList);

    BookmarkResponseDto.ContentList ofContentList(BookmarkInfo.Position position);

    // Entity <-> Info, Info <-> Info
    @Mapping(target = "bookmarkId", source = "id")
    BookmarkInfo.Position buildPosition(BookmarkEntity bookmark);

    // Internal Method =================================================================================================

    @Named("toIsActive")
    default Boolean toIsActive(ContentStatus contentStatus) {
        return contentStatus == ContentStatus.ACTIVATED;
    }
}
