package team.stephen.sunshine.conf.datasource;

import org.apache.ibatis.annotations.Mapper;
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
 * sunshine的sqlsessionfactory配置
 *
 * @author stephen
 * @date 2018/5/26
 */
@Configuration
@MapperScan(basePackages = {"team.stephen.sunshine.dao.sunshine"}, sqlSessionFactoryRef = "sunshineSqlFactoryRef")
public class MybatisSunshineDBConfig {
    @Autowired
    @Qualifier("sunshineDS")
    private DataSource sunshineDataSource;

    @Bean
    public SqlSessionFactory sunshineSqlFactoryRef() throws Exception {
        SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
        //sunshine 数据源
        factoryBean.setDataSource(sunshineDataSource);

        return factoryBean.getObject();
    }

    @Bean
    public SqlSessionTemplate sqlSessionTemplate1() throws Exception {
        return new SqlSessionTemplate(sunshineSqlFactoryRef());
    }
}
