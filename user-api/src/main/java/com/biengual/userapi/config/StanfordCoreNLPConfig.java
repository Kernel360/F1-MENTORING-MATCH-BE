package com.biengual.userapi.config;

import java.util.Properties;

import org.springframework.context.annotation.Configuration;

import edu.stanford.nlp.pipeline.StanfordCoreNLP;

/**
 * 단어 및 문장 분석을 위한 stanford CoreNLP 설정
 *
 * @author 문찬욱
 */
@Deprecated
@Configuration
public class StanfordCoreNLPConfig {

    // @Bean
    public StanfordCoreNLP stanfordCoreNLP() {
        Properties props = new Properties();
        props.setProperty("annotators", "tokenize, ssplit, pos, lemma");
        return new StanfordCoreNLP(props);
    }
}
