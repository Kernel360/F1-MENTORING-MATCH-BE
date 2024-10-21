package com.biengual.userapi.scrap.service;

import java.util.List;

import com.biengual.userapi.scrap.domain.dto.ScrapRequestDto;
import com.biengual.userapi.scrap.domain.dto.ScrapResponseDto;

public interface ScrapService{
	List<ScrapResponseDto.ScrapViewResponseDto> getAllScraps(Long userId);

	ScrapResponseDto.ScrapCreateResponseDto createScrap(Long userId, ScrapRequestDto.ScrapCreateRequestDto requestDto);

	void deleteScrap(Long userId, ScrapRequestDto.ScrapDeleteRequestDto requestDto);

	boolean existsScrap(Long userId, Long contentId);
}
