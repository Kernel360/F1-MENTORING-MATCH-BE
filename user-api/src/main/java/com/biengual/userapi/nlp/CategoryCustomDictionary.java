package com.biengual.userapi.nlp;

import java.util.HashMap;
import java.util.Map;

/**
 * category 분류에서 특정 lemma는 다른 단어로 사용하기 위한 사전 클래스
 *
 * @author 문찬욱
 */
public class CategoryCustomDictionary {
    private final Map<String, String> dictionary = new HashMap<>();

    // TODO: 싱글톤 패턴을 사용할 건지, 그냥 분류할 때마다 생성자로 만들것인지?
    public CategoryCustomDictionary() {
        dictionary.put("sport", "sports");
    }

    // 단어 변환 메서드
    public String replace(String word) {
        return dictionary.getOrDefault(word.toLowerCase(), word);
    }
}
