package com.biengual.userapi.core.entity.question;

import org.springframework.data.mongodb.core.mapping.Document;

import com.biengual.userapi.core.common.enums.QuestionType;
import com.biengual.userapi.core.entity.MongoBaseDocument;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Document(collection = "question")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class QuestionDocument extends MongoBaseDocument {

	private String question;
	private String questionKo;
	private String answer;
	private QuestionType type;

	@Builder
	public QuestionDocument(String question, String questionKo, String answer, QuestionType type) {
		this.question = question;
		this.questionKo = questionKo;
		this.answer = answer;
		this.type = type;
	}

	public static QuestionDocument of(String question, String questionKo, String answer, QuestionType type) {
		return QuestionDocument.builder()
			.question(question)
			.questionKo(questionKo)
			.answer(answer)
			.type(type)
			.build();
	}
}
