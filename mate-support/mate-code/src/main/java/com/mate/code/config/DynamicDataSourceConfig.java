package com.mate.code.config;

import com.baomidou.dynamic.datasource.creator.DefaultDataSourceCreator;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.mate.code.datasource.MasterDataSourceProvider;

@Configuration
public class DynamicDataSourceConfig {

    @Bean
    public MasterDataSourceProvider masterDataSourceProvider(DataSourceProperties dataSourceProperties,
                                                             DefaultDataSourceCreator defaultDataSourceCreator) {
        return new MasterDataSourceProvider(dataSourceProperties, defaultDataSourceCreator);
    }
}
