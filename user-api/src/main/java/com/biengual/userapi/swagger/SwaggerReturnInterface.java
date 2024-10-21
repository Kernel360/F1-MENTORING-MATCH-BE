package com.biengual.userapi.swagger;

import lombok.Getter;

@Getter
public class SwaggerReturnInterface<T> {
	private String code;
	private String message;
	private T data;

}