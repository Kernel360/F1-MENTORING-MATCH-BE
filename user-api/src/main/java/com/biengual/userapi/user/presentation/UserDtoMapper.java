package com.biengual.userapi.user.presentation;

import com.biengual.userapi.oauth2.domain.info.OAuth2UserPrincipal;
import com.biengual.userapi.user.domain.UserCommand;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

/*
* RequestDto를 Command로 바꿔주는 Mapper
*
* @author 문찬욱
*/
@Mapper(
    componentModel = "spring",
    injectionStrategy = InjectionStrategy.CONSTRUCTOR,
    unmappedTargetPolicy = ReportingPolicy.ERROR
)
public interface UserDtoMapper {

    @Mapping(target = "username", source = "request.username")
    @Mapping(target = "email", source = "principal.email")
    UserCommand.UpdateMyInfo of(UserRequestDto.UpdateMyInfo request, OAuth2UserPrincipal principal);
}
