package com.biengual.core.domain.document.content.script;

import org.springframework.data.annotation.TypeAlias;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@TypeAlias("CNNScript")
public class CNNScript implements ReadingScript {
    private String enScript;
    private String koScript;

    public static ReadingScript of(String enScript, String koScript) {
        return CNNScript.builder()
            .enScript(enScript)
            .koScript(koScript)
            .build();
    }

    // Open Search 에서 검색 하기 위한 JSON 설정
    @JsonCreator
    public static CNNScript create(
        @JsonProperty("enScript") String enScript,
        @JsonProperty("koScript") String koScript
    ) {
        return CNNScript.builder()
            .enScript(enScript)
            .koScript(koScript)
            .build();
    }

    @Override
    public String getEnScript() {
        return this.enScript;
    }
    @Override
    public String getKoScript() {
        return this.koScript;
    }
}
