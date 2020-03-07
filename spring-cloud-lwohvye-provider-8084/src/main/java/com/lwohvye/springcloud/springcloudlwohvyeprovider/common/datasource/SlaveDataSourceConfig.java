package com.lwohvye.springcloud.springcloudlwohvyeprovider.common.datasource;

import com.alibaba.druid.pool.DruidDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;
import java.util.Objects;

/**
 * @author Hongyan Wang
 * @packageName com.lwohvye.springcloud.springcloudlwohvyeprovider.common.datasource
 * @className SlaveDataSourceConfig
 * @description
 * @date 2020/1/8 22:57
 */
@Configuration
@MapperScan(basePackages = {"com.lwohvye.springcloud.springcloudlwohvyeprovider.dao.slave"}, sqlSessionTemplateRef = "slaveSqlSessionTemplate")
public class SlaveDataSourceConfig {

    @Autowired
    private Environment env;

    /**
     * 创建数据源(数据源的名称：方法名可以取为XXXDataSource(),XXX为数据库名称,该名称也就是数据源的名称)
     *
     * @Primary 注解用于标识默认使用的 DataSource Bean，因为有三个 DataSource Bean，该注解可用于 master(主数据源)
     * 或 slave(副数据源) DataSource Bean, 但不能用于 dynamicDataSource Bean, 否则会产生循环调用
     * 使用@ConfigurationProperties的prefix参数设置要匹配的属性的前缀
     */
    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.druid.slave")
    public DataSource slaveDataSource() {
        return new DruidDataSource();
    }

    @Bean(name = "slaveTransactionManager")
    public DataSourceTransactionManager slaveTransactionManager() {
        return new DataSourceTransactionManager(slaveDataSource());
    }

    @Bean(name = "slaveSqlSessionFactory")
    public SqlSessionFactory slaveSqlSessionFactory(@Qualifier("slaveDataSource") DataSource dataSource)
            throws Exception {
        SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
        factoryBean.setDataSource(dataSource);
        // 下边两句仅仅用于*.xml文件，如果整个持久层操作不需要使用到xml文件的话（只用注解就可以搞定），则不加
        factoryBean.setTypeAliasesPackage(env.getProperty("mybatis.type-aliases-package"));// 指定基包
        // 配置mybatis返回Map是值为空时显示key
        org.apache.ibatis.session.Configuration configuration = new org.apache.ibatis.session.Configuration();
        configuration.setCallSettersOnNulls(true);
        factoryBean.setConfiguration(configuration);
        factoryBean.setMapperLocations(
                new PathMatchingResourcePatternResolver().getResources(Objects.requireNonNull(env.getProperty("mybatis.slave.mapper-locations"))));
        // 配置数据库下划线转驼峰命名
        Objects.requireNonNull(factoryBean.getObject()).getConfiguration().setMapUnderscoreToCamelCase(true);
        return factoryBean.getObject();
    }

    @Bean(name = "slaveSqlSessionTemplate")
    public SqlSessionTemplate slaveSqlSessionTemplate(@Qualifier("slaveSqlSessionFactory") SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }
}