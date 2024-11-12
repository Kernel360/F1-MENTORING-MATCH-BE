package com.biengual.userapi.question.presentation;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import com.biengual.userapi.question.domain.QuestionCommand;
import com.biengual.userapi.question.domain.QuestionInfo;

/**
 * do~ : Command <- Request
 * of~ : Response <- Info
 * build~ :  Entity <-> Info, Info <-> Info
 * <p>
 * QuestionDto 와 Info, Command 간의 Mapper
 *
 * @author 김영래
 */
@Mapper(
    componentModel = "spring",
    injectionStrategy = InjectionStrategy.CONSTRUCTOR,
    unmappedTargetPolicy = ReportingPolicy.ERROR
)
public interface QuestionDtoMapper {
    // Command <- Request
    QuestionCommand.Verify doVerify(String questionId, String answer);

    // Response <- Info
    QuestionResponseDto.ViewListRes ofViewListRes(QuestionInfo.DetailInfo info);

    // Entity <-> Info, Info <-> Info

}
