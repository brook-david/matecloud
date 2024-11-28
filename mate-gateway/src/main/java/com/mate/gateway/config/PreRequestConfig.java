package com.mate.gateway.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import com.mate.core.cloud.props.MateRequestProperties;
import com.mate.core.cloud.props.MateApiProperties;

/**
 * 预请求配置
 *
 * @author pangu
 */
@Configuration
@EnableConfigurationProperties({MateRequestProperties.class, MateApiProperties.class})
public class PreRequestConfig {
}
