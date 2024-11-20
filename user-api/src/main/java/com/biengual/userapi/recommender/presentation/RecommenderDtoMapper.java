package com.biengual.userapi.recommender.presentation;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import com.biengual.userapi.recommender.domain.RecommenderInfo;

/**
 * do~ : Command <- Request
 * of~ : Response <- Info
 * build~ :  Entity <-> Info, Info <-> Info
 * <p>
 * RecommenderDto 와 Info, Command 간의 Mapper
 *
 * @author 김영래
 */
@Mapper(
    componentModel = "spring",
    injectionStrategy = InjectionStrategy.CONSTRUCTOR,
    unmappedTargetPolicy = ReportingPolicy.ERROR
)
public interface RecommenderDtoMapper {
    // Command <- Request

    // Response <- Info
    GetPreviewDto.Response ofPreviewRes(RecommenderInfo.PreviewRecommender info);

    GetPopularDto.Response ofGetPopularRes(RecommenderInfo.PopularBookmarkRecommender popularBookmarks);

    // Entity <-> Info, Info <-> Info

}
