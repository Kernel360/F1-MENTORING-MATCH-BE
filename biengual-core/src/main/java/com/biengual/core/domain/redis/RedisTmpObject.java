package com.biengual.core.domain.redis;

import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.Builder;

import java.util.List;


public class RedisTmpObject {
    public record TmpList(
        String value
    ) {}

    @Builder
    @JsonTypeName(value = "RedisExample")
    public record RedisExample(
        String subName,
        Integer age,
        List<TmpList> tmpList

    ) implements RedisDto {

        public static RedisExample create(String subName, Integer age, List<TmpList> tmpList) {
            return RedisExample.builder()
                .subName(subName)
                .age(age)
                .tmpList(tmpList)
                .build();
        }
    }

}

