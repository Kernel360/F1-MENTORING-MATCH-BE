package com.biengual.userapi.dashboard.presentation;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

import com.biengual.userapi.dashboard.domain.DashboardInfo;
import com.biengual.userapi.dashboard.presentation.dto.GetCategoryLearningDto;
import com.biengual.userapi.dashboard.presentation.dto.GetCurrentPointDto;
import com.biengual.userapi.dashboard.presentation.dto.GetMissionCalendarDto;
import com.biengual.userapi.dashboard.presentation.dto.GetMonthlyPointHistoryDto;
import com.biengual.userapi.dashboard.presentation.dto.GetQuestionSummaryDto;
import com.biengual.userapi.dashboard.presentation.dto.GetRecentLearningDto;
import com.biengual.userapi.dashboard.presentation.dto.GetRecentLearningSummaryDto;

/**
 * 객체 간의 Mapper를 정의
 *
 * 메서드 네이밍은 prefix + target (inner)class name
 *
 * prefix로 어떤 객체 간의 매핑인지 구분
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
public interface DashboardDtoMapper {

    // Response <- Info
    @Mapping(target = "learningRate", source = "completedLearningRate")
    GetRecentLearningSummaryDto.Response ofRecentLearningSummaryRes(
        DashboardInfo.RecentLearningSummary recentLearningSummary
    );

    GetRecentLearningDto.Response ofRecentLearningRes(DashboardInfo.RecentLearningList recentLearningList);

    @Mapping(target = "duration", source = "videoDurationInSeconds", qualifiedByName = "toDurationFormat")
    GetRecentLearningDto.RecentLearning ofRecentLearning(DashboardInfo.RecentLearning recentLearning);

    GetCategoryLearningDto.Response ofCategoryLearningRes(DashboardInfo.CategoryLearningList categoryLearningList);

    GetCurrentPointDto.Response ofCurrentPointRes(Long currentPoint);

    @Mapping(target = "monthlyHistoryList", source = "missionHistoryList")
    GetMissionCalendarDto.Response ofMissionCalendarRes(DashboardInfo.MissionCalendar missionCalendar);

    @Mapping(target = "date", source = "date", qualifiedByName = "toLocalDateFormat")
    GetMissionCalendarDto.MissionHistory ofMissionHistory(DashboardInfo.MissionHistory missionHistory);

    GetQuestionSummaryDto.Response ofQuestionSummary(DashboardInfo.QuestionSummary info);
    
    @Mapping(target = "monthlyHistoryList", source = "dailyPointHistoryList")
    GetMonthlyPointHistoryDto.Response ofMonthlyPointHistoryRes(DashboardInfo.MonthlyPointHistory monthlyPointHistory);

    // Internal Method =================================================================================================

    @Named("toDurationFormat")
    default String toVideoDurationFormat(Integer videoDurationInSeconds) {
        if (videoDurationInSeconds != null) {
            int minutes = videoDurationInSeconds / 60;
            int seconds = videoDurationInSeconds % 60;
            return String.format("%d:%02d", minutes, seconds);
        }
        return null;
    }

    @Named("toLocalDateFormat")
    default LocalDate toLocalDateFormat(LocalDateTime localDateTime) {
        return localDateTime.toLocalDate();
    }
}
