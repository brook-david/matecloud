package com.mate.core.web.config;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import com.mate.core.common.factory.YamlPropertySourceFactory;
import com.mate.core.web.handler.BaseExceptionHandler;

/**
 * 统一异常处理配置
 * @author xuzhanfu
 */
@AutoConfiguration
@ComponentScan(value= "com.mate.core.web.handler")
@PropertySource(factory = YamlPropertySourceFactory.class, value = "classpath:mate-error.yml")
public class ExceptionConfiguration {

    @Bean
    public BaseExceptionHandler baseExceptionHandler(){
        return new BaseExceptionHandler();
    }
}