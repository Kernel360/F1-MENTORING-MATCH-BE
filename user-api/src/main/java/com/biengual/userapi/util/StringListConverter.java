package com.biengual.userapi.util;

import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Deprecated
@Converter
public class StringListConverter implements AttributeConverter<List<String>, String> {

	private final ObjectMapper mapper = new ObjectMapper();

	@Override
	public String convertToDatabaseColumn(List<String> contentList) {
		try {
			return mapper.writeValueAsString(contentList);
		} catch (JsonProcessingException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public List<String> convertToEntityAttribute(String content) {
		try {
			return mapper.readValue(content, List.class);
		} catch (JsonProcessingException e) {
			throw new RuntimeException(e);
		}
	}
}
