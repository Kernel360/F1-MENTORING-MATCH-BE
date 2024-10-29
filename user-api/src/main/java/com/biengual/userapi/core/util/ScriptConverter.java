package com.biengual.userapi.core.util;

import com.biengual.userapi.core.domain.document.content.script.BaseScript;
import com.biengual.userapi.core.domain.document.content.script.CNNScript;
import com.biengual.userapi.core.domain.document.content.script.Script;
import com.biengual.userapi.core.domain.document.content.script.YoutubeScript;
import org.bson.Document;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class ScriptConverter implements Converter<Document, Script> {

    @Override
    public Script convert(Document source) {
        String className = source.getString("_class"); // _class 필드에서 지정한 이름 가져오기

        if ("CNNScript".equals(className)) {
            return convertToCNNScript(source);
        } else if ("YoutubeScript".equals(className)) {
            return convertToYoutubeScript(source);
        }

        return convertToBaseScript(source);
    }

    private CNNScript convertToCNNScript(Document source) {
        return (CNNScript) CNNScript.of(source.getString("enScript"), source.getString("koScript"));
    }

    private YoutubeScript convertToYoutubeScript(Document source) {
        return (YoutubeScript) YoutubeScript.of(
            source.getDouble("startTimeInSecond"),
            source.getDouble("durationInSecond"),
            source.getString("enScript"),
            source.getString("koScript")
        );
    }

    private BaseScript convertToBaseScript(Document source) {
        return (BaseScript) BaseScript.of(
            source.containsKey("startTimeInSecond") ? source.getDouble("startTimeInSecond") : null,
            source.containsKey("durationInSecond") ? source.getDouble("durationInSecond") : null,
            source.getString("enScript"),
            source.getString("koScript")
        );
    }
}
