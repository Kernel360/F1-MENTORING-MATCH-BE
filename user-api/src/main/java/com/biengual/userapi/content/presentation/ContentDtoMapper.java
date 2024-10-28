package com.biengual.userapi.content.presentation;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import com.biengual.userapi.content.domain.ContentCommand;

/**
 * do~ : Command <- Request
 * of~ : Response <- Info
 * build~ :  Entity <-> Info, Info <-> Info
 * <p>
 * ContentDto, CrawlingDto 와 Info, Command 간의 Mapper
 *
 * @author 김영래
 */
@Mapper(
	componentModel = "spring",
	injectionStrategy = InjectionStrategy.CONSTRUCTOR,
	unmappedTargetPolicy = ReportingPolicy.ERROR
)
public interface ContentDtoMapper {
	// Command <- Request
	ContentCommand.CrawlingContent doCrawlingContent(ContentRequestDto.CreateReq request);

	// Response <- Info
	// Entity <-> Info, Info <-> Info

}
