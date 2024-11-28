package com.mate.core.kafka.config;

import org.springframework.context.annotation.PropertySource;
import com.mate.core.common.factory.YamlPropertySourceFactory;

@PropertySource(factory = YamlPropertySourceFactory.class, value = "classpath:mate-kafka.yml")
public class KafkaConfiguration {
}
