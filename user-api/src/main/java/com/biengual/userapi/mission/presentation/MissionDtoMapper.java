package com.biengual.userapi.mission.presentation;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import com.biengual.userapi.mission.domain.MissionInfo;

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
    Status.Response ofStatus(MissionInfo.StatusInfo info);

    // Response <- Info


    // Entity <-> Info, Info <-> Info

}
