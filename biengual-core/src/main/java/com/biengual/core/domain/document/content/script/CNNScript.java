package com.biengual.core.domain.document.content.script;

import org.springframework.data.annotation.TypeAlias;

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

    @Override
    public String getEnScript() {
        return this.enScript;
    }
    @Override
    public String getKoScript() {
        return this.koScript;
    }
}
