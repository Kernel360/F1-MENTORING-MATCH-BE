package com.biengual.userapi.util;

import java.util.List;
import java.util.Random;

import org.springframework.stereotype.Component;

import lombok.Getter;

@Component
@Getter
public class RandomNicknameGenerator {
	private static final List<String> prefixNicknames
		= List.of("빠른", "용감한", "친절한", "행복한", "빛나는");
	private static final List<String> suffixNicknames
		= List.of("여우", "강아지", "고양이", "다람쥐", "도마뱀");
	private static final Random random = new Random();

	public static String setRandomNickname() {
		return prefixNicknames.get((int)(Math.random() * 5)) +
			" " + suffixNicknames.get((int)(Math.random() * 5)) +
			" " + String.format("%04d", random.nextInt(10000));
	}

}
