package com.biengual.userapi.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import lombok.extern.slf4j.Slf4j;

/**
 * 단어 및 문장 유사도 분석을 위한 Word2Vec 설정
 *
 * MODEL_FILE_PATH에 쓸 파일이 필요합니다.
 *
 * @author 문찬욱
 */
@Slf4j
@Deprecated
@Configuration
public class Word2VecConfig {
    @Value("${nlp.model.word2vec}")
    public String modelPath;

    // @Bean
    // public Word2Vec word2Vec() {
    //     File modelFile = new File(modelPath);
    //     log.error("WORD2VEC");
    //     return WordVectorSerializer.readWord2VecModel(modelFile);
    // }
}
