package team.stephen.sunshine.conf.datasource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

/**
 * 其他的的sqlsessionfactory配置
 *
 * @author stephen
 * @date 2018/5/26
 */
@Configuration
@MapperScan(basePackages = {"team.stephen.sunshine.dao.other"}, sqlSessionFactoryRef = "otherSqlFactoryRef")
public class MybatisOtherDBConfig {
    @Autowired
    @Qualifier("otherDS")
    private DataSource otherDataSource;

    @Bean
    public SqlSessionFactory otherSqlFactoryRef() throws Exception {
        SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
        //sunshine 数据源
        factoryBean.setDataSource(otherDataSource);

        return factoryBean.getObject();
    }

    @Bean
    public SqlSessionTemplate sqlSessionTemplate1() throws Exception {
        return new SqlSessionTemplate(otherSqlFactoryRef());
    }
}
