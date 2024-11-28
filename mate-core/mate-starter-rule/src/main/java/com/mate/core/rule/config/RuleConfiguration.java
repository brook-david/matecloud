package com.mate.core.rule.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.mate.core.rule.service.IRuleCacheService;
import com.mate.core.rule.service.impl.RuleCacheServiceImpl;

/**
 * 规则配置
 * @author pangu
 */
@Configuration
public class RuleConfiguration {

    @Bean
    public IRuleCacheService ruleCacheService() {
        return new RuleCacheServiceImpl();
    }
}
