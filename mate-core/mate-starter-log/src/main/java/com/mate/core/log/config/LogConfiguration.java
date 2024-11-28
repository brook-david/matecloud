package com.mate.core.log.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import com.mate.core.log.aspect.LogAspect;
import com.mate.core.log.event.LogListener;
import com.mate.core.log.feign.ICommonLogProvider;
import com.mate.core.log.feign.ISysLogProvider;
import com.mate.core.log.props.LogProperties;
import com.mate.core.log.props.LogType;

/**
 * 日志配置中心
 * @author pangu
 */
@EnableAsync
@AutoConfiguration
@RequiredArgsConstructor
@ConditionalOnWebApplication
@EnableConfigurationProperties(value = LogProperties.class)
public class LogConfiguration {

    private final ISysLogProvider sysLogProvider;
    private final ICommonLogProvider commonLogProvider;

    private final LogProperties logProperties;

    @Bean
    public LogListener sysLogListener() {
        if (logProperties.getLogType().equals(LogType.KAFKA)) {
            return new LogListener(commonLogProvider, logProperties);
        }
        return new LogListener(sysLogProvider, logProperties);

    }

    @Bean
    public LogAspect logAspect(ApplicationContext applicationContext){
        return new LogAspect(applicationContext);
    }
}
