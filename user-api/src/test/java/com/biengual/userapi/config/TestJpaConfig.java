package com.biengual.userapi.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.context.TestConfiguration;

@TestConfiguration
@EntityScan(basePackages = {"com.biengual.core"})
public class TestJpaConfig {
}
