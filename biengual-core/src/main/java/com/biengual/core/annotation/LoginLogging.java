package com.biengual.core.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * AOP에서 Login 로그를 찍기 위한 어노테이션
 *
 * @author 문찬욱
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface LoginLogging {
    String value() default "";
}
