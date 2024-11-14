package com.biengual.userapi.question.presentation;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import com.biengual.userapi.oauth2.info.OAuth2UserPrincipal;
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
    @Mapping(target = "userId", source = "principal.id")
    QuestionCommand.Verify doVerify(VerifyDto.Request request, OAuth2UserPrincipal principal);

    @Mapping(target = "userId", source = "principal.id")
    QuestionCommand.GetHint doGetHint(GetHintDto.Request request, OAuth2UserPrincipal principal);

    @Mapping(target = "userId", source = "principal.id")
    QuestionCommand.GetQuestion doGetQuestion(Long contentId, OAuth2UserPrincipal principal);

    // Response <- Info
    QuestionResponseDto.ViewListRes ofViewListRes(QuestionInfo.DetailInfo info);

    GetHintDto.Response ofGetHintRes(QuestionInfo.Hint info);

    // Entity <-> Info, Info <-> Info

}
