package com.mate.core.cloud.config;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import com.mate.core.cloud.filter.TraceFilter;
import com.mate.core.cloud.props.MateRequestProperties;

/**
 * 请求配置，包括tracId和其他网络请求
 * @author pangu
 */
@AutoConfiguration
@EnableConfigurationProperties(MateRequestProperties.class)
public class RequestConfiguration {

//    @Bean
//    public TenantContextHolderFilter tenantContextHolderFilter() {
//        return new TenantContextHolderFilter();
//    }

    @Bean
    public TraceFilter traceFilter() {
        return new TraceFilter();
    }

}