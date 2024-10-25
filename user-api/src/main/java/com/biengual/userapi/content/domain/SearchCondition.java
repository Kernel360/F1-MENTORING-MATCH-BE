package com.biengual.userapi.content.domain;

import java.util.List;

import lombok.Getter;

@Getter
public class SearchCondition {
	private List<String> script;
	private String title;
}
