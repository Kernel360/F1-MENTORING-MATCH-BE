package com.biengual.userapi.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RedissonConfig {
    @Value("${spring.data.redis.host}")
    private String host;

    @Value("${spring.data.redis.port}")
    private int port;

    @Value("${spring.data.redis.password}")
    private String password;

    @Value("${spring.data.redis.username}")
    private String username;

    @Bean
    public RedissonClient redissonClient() {
        String url = "redis://" + host + ":" + port;

        Config config = new Config();
        config.useSingleServer()
            .setAddress(url)
            .setUsername(username)
            .setPassword(password);

        return Redisson.create(config);
    }
}
