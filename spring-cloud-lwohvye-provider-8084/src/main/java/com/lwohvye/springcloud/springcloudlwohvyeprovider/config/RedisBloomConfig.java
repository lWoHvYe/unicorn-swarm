package com.lwohvye.springcloud.springcloudlwohvyeprovider.config;

import com.google.common.base.Charsets;
import com.google.common.hash.Funnel;
import com.lwohvye.springcloud.springcloudlwohvyeprovider.common.util.BloomFilterHelper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Hongyan Wang
 * @packageName com.lwohvye.springcloud.springcloudlwohvyeprovider.config
 * @className RedisBloomConfig
 * @description
 * @date 2020/2/6 15:35
 */
@Configuration
public class RedisBloomConfig {
    /** 
     * @description 注册BloomFilterHelper,
     *              expectedInsertions为欲放入过滤器中的数据量,  fpp为误差率，值越小占用空间越大
     * @params []
     * @return com.lwohvye.springcloud.springcloudlwohvyeprovider.common.util.BloomFilterHelper<java.lang.String>
     * @author Hongyan Wang 
     * @date 2020/2/6 15:36
     */
    @Bean
    public BloomFilterHelper<String> initBloomFilterHelper() {
        return new BloomFilterHelper<>((Funnel<String>) (from, into) -> into.putString(from, Charsets.UTF_8)
                .putString(from, Charsets.UTF_8), 100, 0.01);
    }
}
