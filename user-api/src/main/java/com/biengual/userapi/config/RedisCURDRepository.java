package com.biengual.userapi.config;

import com.biengual.core.domain.redis.RedisDto;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RedisCURDRepository {
    private final RedisTemplate<String, Object> redisTemplate;
    private final RedisTemplate<String, RedisDto> redisDtoTemplate;

    public <T> T findByKey(String key) {
        T value = (T) redisDtoTemplate.opsForValue().get(key);
//        String redisValue = "{\"name\":\"John\",\"age\":\"25\"";
        if (ObjectUtils.isEmpty(value)) {
            return null;
        } else {
            return value;
        }
    }

//    public <T> T findListByKey(String key, Class<T> classType) {
//        List<Object> range = redisTemplate.opsForList().range(key, 0, -1);
//        if (ObjectUtils.isEmpty(range)) {
//            return null;
//        } else {
//            return objectMapper.readValue(range, classType);
//        }
//    }

//    public void save(String key, Object value) throws JsonProcessingException {
//        redisTemplate.opsForValue().set(key, objectMapper.writeValueAsString(value));
//    }

    public void save(String key, Object value) {
        if (value instanceof RedisDto redisDto) {
            redisDtoTemplate.opsForValue().set(key, redisDto);
        } else {
            redisTemplate.opsForValue().set(key, value);
        }
    }

//    public void saveList(String key, List<Object> value) {
//        redisTemplate.opsForList().rightPushAll(key, value);
//    }
}
