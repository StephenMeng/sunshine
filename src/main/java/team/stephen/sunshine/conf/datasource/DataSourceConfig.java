package team.stephen.sunshine.conf.datasource;

import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 多数据源配置
 *
 * @author stephen
 * @date 2018/5/22
 */
@Configuration
public class DataSourceConfig {
    @Bean(name = "sunshineDS")
    @ConfigurationProperties(prefix = "spring.datasource.sunshine")
    public javax.sql.DataSource sunshineDataSource() {
        return DataSourceBuilder.create().build();
    }


    @Bean(name = "otherDS")
    @ConfigurationProperties(prefix = "spring.datasource.other")
    public javax.sql.DataSource otherDataSource() {
        return DataSourceBuilder.create().build();
    }
}
