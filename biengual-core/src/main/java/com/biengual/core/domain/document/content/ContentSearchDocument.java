package com.biengual.core.domain.document.content;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.biengual.core.domain.document.content.script.CNNScript;
import com.biengual.core.domain.entity.content.ContentEntity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ContentSearchDocument {

    @JsonProperty("id")
    private String id;

    @JsonProperty("title")
    private String title;

    @JsonProperty("categoryName")
    private String categoryName;

    @JsonProperty("scripts")
    private List<CNNScript> scripts;

    @Builder
    public ContentSearchDocument(ContentEntity contentEntity, ContentDocument contentDocument) {
        this.id = contentEntity.getId().toString();
        this.title = contentEntity.getTitle();
        this.categoryName = contentEntity.getCategory().getName();
        this.scripts = contentDocument.getScripts()
            .stream()
            .map(script -> (CNNScript) CNNScript.of(script.getEnScript(), script.getKoScript()))
            .toList();
    }

    public static ContentSearchDocument createdByContents(ContentEntity content, ContentDocument document) {
        return ContentSearchDocument.builder()
            .contentEntity(content)
            .contentDocument(document)
            .build();
    }
}