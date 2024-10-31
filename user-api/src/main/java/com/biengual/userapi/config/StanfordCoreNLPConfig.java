package com.biengual.userapi.config;

import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

/**
 * 단어 및 문장 분석을 위한 stanford CoreNLP 설정
 *
 * @author 문찬욱
 */
@Configuration
public class StanfordCoreNLPConfig {

    @Bean
    public StanfordCoreNLP stanfordCoreNLP() {
        Properties props = new Properties();
        props.setProperty("annotators", "tokenize, ssplit, pos, lemma");
        props.setProperty("ner.useSUTime", "false");
        return new StanfordCoreNLP(props);
    }
}
