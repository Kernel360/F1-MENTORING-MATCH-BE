package com.echall.platform.question.service;

import static com.echall.platform.message.error.code.ContentErrorCode.*;
import static com.echall.platform.message.error.code.QuestionErrorCode.*;
import static com.echall.platform.question.domain.entity.QuestionDocument.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.echall.platform.content.domain.entity.ContentDocument;
import com.echall.platform.content.domain.entity.ContentEntity;
import com.echall.platform.content.domain.enums.ContentStatus;
import com.echall.platform.content.repository.ContentRepository;
import com.echall.platform.content.repository.ContentScriptRepository;
import com.echall.platform.message.error.exception.CommonException;
import com.echall.platform.question.domain.dto.QuestionRequestDto;
import com.echall.platform.question.domain.dto.QuestionResponseDto;
import com.echall.platform.question.domain.entity.QuestionDocument;
import com.echall.platform.question.domain.enums.QuestionType;
import com.echall.platform.question.repository.QuestionRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class QuestionServiceImpl implements QuestionService {
	private final QuestionRepository questionRepository;
	private final ContentRepository contentRepository;
	private final ContentScriptRepository contentScriptRepository;

	@Override
	@Transactional
	public QuestionResponseDto.QuestionCreateResponseDto createQuestion(
		Long contentId, QuestionRequestDto.QuestionCreateRequestDto requestDto
	) {
		Random random = new Random();
		Set<Integer> randomIdxes = new HashSet<>();
		List<String> questionIds = new ArrayList<>();
		List<String> randomScripts = new ArrayList<>();
		List<String> randomKoScripts = new ArrayList<>();

		ContentDocument contentDocument = getContentDocument(contentId);

		while (randomIdxes.size() < requestDto.questionNumOfBlank() + requestDto.questionNumOfOrder()) {
			randomIdxes.add(random.nextInt(contentDocument.getScripts().size()));
		}

		for (Integer idx : randomIdxes) {
			randomScripts.add(contentDocument.getScripts().get(idx).getEnScript());
			randomKoScripts.add(contentDocument.getScripts().get(idx).getKoScript());
		}

		int start = 0;
		// make blank question
		questionIds.addAll(makeBlankQuestion(randomScripts, randomKoScripts, requestDto.questionNumOfBlank()));
		start += requestDto.questionNumOfBlank();

		// make word order question
		questionIds.addAll(
			makeWordOrderQuestion(randomScripts, randomKoScripts, start, requestDto.questionNumOfOrder()));

		// update QuestionIds
		contentDocument.updateQuestionIds(questionIds);
		contentScriptRepository.save(contentDocument);

		return new QuestionResponseDto.QuestionCreateResponseDto(questionIds);
	}

	@Override
	@Transactional(readOnly = true)
	public List<QuestionResponseDto.QuestionViewResponseDto> getQuestions(Long contentId) {
		ContentDocument contentDocument = getContentDocument(contentId);
		List<String> questionDocumentIds = contentDocument.getQuestionIds();
		List<QuestionResponseDto.QuestionViewResponseDto> questions = new ArrayList<>();

		for (String questionDocumentId : questionDocumentIds) {
			QuestionDocument questionDocument = questionRepository.findById(new ObjectId(questionDocumentId))
				.orElseThrow(() -> new CommonException(QUESTION_NOT_FOUND));
			questions.add(
				new QuestionResponseDto.QuestionViewResponseDto(
					questionDocument.getQuestion(),
					questionDocument.getAnswer(),
					questionDocument.getType()
				)
			);
		}
		return questions;
	}

	// Internal Methods ------------------------------------------------------------------------------------------------
	private List<String> makeBlankQuestion(
		List<String> randomScripts, List<String> randomKoScripts, Integer numOfBlank
	) {
		List<String> questionIds = new ArrayList<>();
		Random random = new Random();

		for (int i = 0; i < numOfBlank; ++i) {
			String script = randomScripts.get(i);
			String[] words = script.split("\\s+");
			if (words.length > 1) {
				int blankIndex = random.nextInt(words.length);
				StringBuilder question = new StringBuilder();
				for (int j = 0; j < words.length; j++) {
					if (j == blankIndex) {
						question.append("____");
					} else {
						question.append(words[j]);
					}
					if (j < words.length - 1) {
						question.append(" ");
					}
				}
				questionIds.add(
					saveToMongo(
						questionRepository,
						question.toString(), randomKoScripts.get(i), words[blankIndex],
						QuestionType.BLANK
					)
				);
			}
		}
		return questionIds;
	}

	private List<String> makeWordOrderQuestion(
		List<String> randomScripts, List<String> randomKoScripts, Integer start, Integer numOfOrder
	) {
		List<String> questionIds = new ArrayList<>();
		Random random = new Random();

		for (int i = start; i < start + numOfOrder; ++i) {
			String script = randomScripts.get(i);
			String[] words = script.split("\\s+");
			if (words.length > 1) {
				List<String> shuffledWords = new ArrayList<>(Arrays.asList(words));
				Collections.shuffle(shuffledWords, random);
				String question = String.join(" ", shuffledWords);

				questionIds.add(
					saveToMongo(questionRepository, question, randomKoScripts.get(i), script, QuestionType.ORDER)
				);
			}
		}
		return questionIds;
	}

	private String saveToMongo(
		QuestionRepository questionRepository, String question, String questionKo, String word, QuestionType type) {

		QuestionDocument questionDocument = of(question, questionKo, word, type);
		questionRepository.save(questionDocument);

		return questionDocument.getId().toString();
	}

	private ContentDocument getContentDocument(Long contentId) {
		ContentEntity content = contentRepository.findById(contentId)
			.orElseThrow(() -> new CommonException(CONTENT_NOT_FOUND));
		content.updateStatus(ContentStatus.ACTIVATED);

		return contentScriptRepository.findById(new ObjectId(content.getMongoContentId()))
			.orElseThrow(() -> new CommonException(CONTENT_NOT_FOUND));
	}
}
