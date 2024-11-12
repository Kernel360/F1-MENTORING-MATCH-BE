package com.biengual.core.domain.document.question;

import java.util.List;

import org.springframework.data.mongodb.core.mapping.Document;

import com.biengual.core.domain.document.MongoBaseDocument;
import com.biengual.core.enums.QuestionType;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Document(collection = "question")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class QuestionDocument extends MongoBaseDocument {

    private String question;
    private List<String> examples;
    private String answer;
    private QuestionType type;

    @Builder
    public QuestionDocument(String question, List<String> examples, String answer, QuestionType type) {
        this.question = question;
        this.examples = examples;
        this.answer = answer;
        this.type = type;
    }

    public static QuestionDocument of(String question, List<String> examples, String answer, QuestionType type) {
        return QuestionDocument.builder()
            .question(question)
            .examples(examples)
            .answer(answer)
            .type(type)
            .build();
    }
}
