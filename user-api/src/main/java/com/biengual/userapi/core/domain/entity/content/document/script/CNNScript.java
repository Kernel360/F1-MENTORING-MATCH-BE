package com.biengual.userapi.core.domain.entity.content.document.script;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.TypeAlias;

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
