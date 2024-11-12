package com.biengual.userapi.learning.presentation;

import com.biengual.userapi.learning.domain.LearningCommand;
import com.biengual.userapi.learning.presentation.dto.RecordLearningRateDto;
import com.biengual.userapi.oauth2.info.OAuth2UserPrincipal;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

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
public interface LearningDtoMapper {

    @Mapping(target = "userId", source = "principal.id")
    LearningCommand.RecordLearningRate doRecordLearningRate(
        RecordLearningRateDto.Request request, OAuth2UserPrincipal principal
    );
}
