package com.lwohvye.springcloud.springcloudlwohvyeconsumer;

import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.autoconfigure.RefreshAutoConfiguration;

@SpringBootTest
@ImportAutoConfiguration(RefreshAutoConfiguration.class) // fixed UT error: No scope registered for scope name 'refresh'
class SpringCloudLwohvyeConsumerConfigApplicationTests {

    @Test
    void contextLoads() {
    }

}
