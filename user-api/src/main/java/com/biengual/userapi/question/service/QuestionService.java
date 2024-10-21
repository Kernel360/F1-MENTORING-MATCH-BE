package com.biengual.userapi.question.service;

import java.util.List;

import com.biengual.userapi.question.domain.dto.QuestionRequestDto;
import com.biengual.userapi.question.domain.dto.QuestionResponseDto;

public interface QuestionService {
	QuestionResponseDto.QuestionCreateResponseDto createQuestion(
		Long contentId, QuestionRequestDto.QuestionCreateRequestDto requestDto
	);

	List<QuestionResponseDto.QuestionViewResponseDto> getQuestions(Long contentId);
}
