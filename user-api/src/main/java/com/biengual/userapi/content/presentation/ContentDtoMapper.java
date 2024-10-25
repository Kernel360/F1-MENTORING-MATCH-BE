package com.biengual.userapi.content.presentation;

import com.biengual.userapi.content.domain.ContentInfo;
import com.biengual.userapi.content.domain.dto.ContentResponseDto;
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
public interface ContentDtoMapper {
    // 스크랩 많은 순 컨텐츠 조회 매핑
    @Mapping(target = "scrapPreview", source = "previewContents")
    ContentResponseDto.ScrapPreviewContentsRes ofScrapPreviewContentsRes(ContentInfo.PreviewContents previewContents);
}
