package com.echall.platform.scrap.service;

import static com.echall.platform.message.error.code.ScrapErrorCode.*;
import static com.echall.platform.message.error.code.UserErrorCode.*;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.echall.platform.message.error.exception.CommonException;
import com.echall.platform.scrap.domain.dto.ScrapRequestDto;
import com.echall.platform.scrap.domain.dto.ScrapResponseDto;
import com.echall.platform.scrap.domain.entity.ScrapEntity;
import com.echall.platform.scrap.repository.ScrapRepository;
import com.echall.platform.user.domain.entity.UserEntity;
import com.echall.platform.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ScrapServiceImpl implements ScrapService {
	private final ScrapRepository scrapRepository;
	private final UserRepository userRepository;

	@Override
	@Transactional(readOnly = true)
	public List<ScrapResponseDto.ScrapViewResponseDto> getAllScraps(Long userId) {
		List<ScrapEntity> scrap = scrapRepository.findByUserId(userId);
		if (scrap.isEmpty()) {
			throw new CommonException(SCRAP_NOT_FOUND);
		}

		List<ScrapResponseDto.ScrapViewResponseDto> scrapDtos = new ArrayList<>();
		for (ScrapEntity scrapEntity : scrap) {
			scrapDtos.add(ScrapResponseDto.ScrapViewResponseDto.of(scrapEntity));
		}

		return scrapDtos;
	}

	@Override
	@Transactional
	public ScrapResponseDto.ScrapCreateResponseDto createScrap(
		String email, ScrapRequestDto.ScrapCreateRequestDto requestDto
	) {
		UserEntity user = userRepository.findByEmail(email)
			.orElseThrow(() -> new CommonException(USER_NOT_FOUND));

		if(scrapRepository.findAlreadyExists(user.getId(), requestDto.contentId())){
			throw new CommonException(SCRAP_ALREADY_EXISTS);
		}

		ScrapEntity scrap = ScrapEntity.builder()
			.contentId(requestDto.contentId())
			.build();

		scrapRepository.save(scrap);
		user.updateUserScrap(scrap);

		return ScrapResponseDto.ScrapCreateResponseDto.of(scrap.getContentId());
	}

	@Override
	@Transactional
	public ScrapResponseDto.ScrapDeleteResponseDto deleteScrap(
		Long userId, ScrapRequestDto.ScrapDeleteRequestDto requestDto
	) {
		return new ScrapResponseDto.ScrapDeleteResponseDto(scrapRepository.deleteScrap(userId, requestDto.scrapId()));
	}
}
