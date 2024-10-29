package com.biengual.userapi.core.swagger;

import lombok.Getter;

@Getter
public class SwaggerReturnInterface<T> {
	private String code;
	private String message;
	private T data;

}