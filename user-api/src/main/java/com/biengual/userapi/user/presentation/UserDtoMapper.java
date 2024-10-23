package com.biengual.userapi.user.presentation;

import com.biengual.userapi.oauth2.domain.info.OAuth2UserPrincipal;
import com.biengual.userapi.user.domain.UserCommand;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

/**
 * 객체 간의 Mapper
 *
 * 접두사로 어떤 객체 간의 매핑인지 구분
 * do~ : Command <- Request
 * of~ : Response <- Info
 * build~ :  Entity <-> Info, Info <-> Info
 *
 * @author 문찬욱
 */
@Mapper(
    componentModel = "spring",
    injectionStrategy = InjectionStrategy.CONSTRUCTOR,
    unmappedTargetPolicy = ReportingPolicy.ERROR
)
public interface UserDtoMapper {

    // 본인 정보 수정 Command 매핑
    @Mapping(target = "username", source = "request.username")
    @Mapping(target = "categoryIds", source = "request.categories")
    @Mapping(target = "userId", source = "principal.id")
    @Mapping(target = "email", source = "principal.email")
    UserCommand.UpdateMyInfo doUpdateMyInfo(UserRequestDto.UpdateMyInfoReq request, OAuth2UserPrincipal principal);
}
