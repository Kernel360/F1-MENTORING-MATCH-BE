package com.biengual.core.domain.redis;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

/**
 * Redis에 단순 String 객체가 아닌 Dto 객체를 넣기위한 인터페이스
 */
@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.PROPERTY,
    property = "@class")
@JsonSubTypes({
//    @JsonSubTypes.Type(value = .class, name = "")
})
public interface RedisObject {
}
