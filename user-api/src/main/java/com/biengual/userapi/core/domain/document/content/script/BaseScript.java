package com.biengual.userapi.core.domain.document.content.script;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BaseScript implements Script {
    private double startTimeInSecond;
    private double durationInSecond;
    private String enScript;
    private String koScript;

    public static Script of(double startTimeInSecond, double durationInSecond, String enScript, String koScript) {
        return BaseScript.builder()
            .startTimeInSecond(startTimeInSecond)
            .durationInSecond(durationInSecond)
            .enScript(enScript)
            .koScript(koScript)
            .build();
    }
}
