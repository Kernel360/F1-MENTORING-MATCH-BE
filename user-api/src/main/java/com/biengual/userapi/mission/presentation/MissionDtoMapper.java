package com.biengual.userapi.mission.presentation;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import com.biengual.userapi.mission.domain.MissionCommand;
import com.biengual.userapi.mission.domain.MissionInfo;
import com.biengual.userapi.oauth2.info.OAuth2UserPrincipal;

/**
 * do~ : Command <- Request
 * of~ : Response <- Info
 * build~ :  Entity <-> Info, Info <-> Info
 * <p>
 * MissionDto 와 Info, Command 간의 Mapper
 *
 * @author 김영래
 */
@Mapper(
    componentModel = "spring",
    injectionStrategy = InjectionStrategy.CONSTRUCTOR,
    unmappedTargetPolicy = ReportingPolicy.ERROR
)
public interface MissionDtoMapper {

    // Command <- Request
    @Mapping(target = "userId", source = "principal.id")
    MissionCommand.Update doUpdate(OAuth2UserPrincipal principal, UpdateDto.Request request);

    // Response <- Info
    StatusDto.Response ofStatus(MissionInfo.StatusInfo info);


    // Entity <-> Info, Info <-> Info

}
