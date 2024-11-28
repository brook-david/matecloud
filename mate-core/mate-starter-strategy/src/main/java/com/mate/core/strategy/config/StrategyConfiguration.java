package com.mate.core.strategy.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.mate.core.strategy.service.BusinessHandler;
import com.mate.core.strategy.service.BusinessHandlerChooser;

import java.util.List;

/**
 * 策略模式自动注入配置
 *
 * @author xuzhanfu
 * @date 2020-9-5
 */
@Configuration
public class StrategyConfiguration {

	@Bean
	public BusinessHandlerChooser businessHandlerChooser(List<BusinessHandler> businessHandlers) {
		BusinessHandlerChooser businessHandlerChooser = new BusinessHandlerChooser();
		businessHandlerChooser.setBusinessHandlerMap(businessHandlers);
		return businessHandlerChooser;
	}

}
