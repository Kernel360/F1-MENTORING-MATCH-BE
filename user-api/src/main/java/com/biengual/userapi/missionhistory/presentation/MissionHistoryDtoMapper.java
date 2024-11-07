package com.biengual.userapi.missionhistory.presentation;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import com.biengual.userapi.missionhistory.domain.MissionHistoryInfo;

/**
 * do~ : Command <- Request
 * of~ : Response <- Info
 * build~ :  Entity <-> Info, Info <-> Info
 * <p>
 * MissionHistoryDto 와 Info, Command 간의 Mapper
 *
 * @author 김영래
 */
@Mapper(
    componentModel = "spring",
    injectionStrategy = InjectionStrategy.CONSTRUCTOR,
    unmappedTargetPolicy = ReportingPolicy.ERROR
)
public interface MissionHistoryDtoMapper {

    // Command <- Request

    // Response <- Info
    RecentHistoryDto.Response ofRecentHistory(MissionHistoryInfo.RecentHistories info);

    // Entity <-> Info, Info <-> Info

}
