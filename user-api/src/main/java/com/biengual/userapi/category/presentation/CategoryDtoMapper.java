package com.biengual.userapi.category.presentation;

import com.biengual.userapi.category.domain.CategoryInfo;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
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
public interface CategoryDtoMapper {
    // 모든 카테고리 조회 매핑
    CategoryResponseDto.AllCategoriesRes ofAllCategoriesRes(CategoryInfo.AllCategories allCategories);
}
