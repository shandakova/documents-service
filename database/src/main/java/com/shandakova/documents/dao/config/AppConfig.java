package com.shandakova.documents.dao.config;

import com.shandakova.documents.ConnectionPool;
import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.Properties;

@Configuration
@ComponentScan(basePackages = "com.shandakova.documents")
@EntityScan(basePackages = {"com.shandakova.documents.entities", "com.shandakova.documents.entities.type"})
@PropertySource("classpath:database.properties")
@EnableJpaRepositories(basePackages = {"com.shandakova.documents.dao.impl.jpa.repository"})
@EnableTransactionManagement
public class AppConfig {
    @Bean
    public DbProperties dbProperties() {
        return new DbProperties();
    }

    @Bean
    public ConnectionPool connectionPool() throws SQLException {
        return new ConnectionPool(dbProperties());
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        LocalContainerEntityManagerFactoryBean emFactory = new LocalContainerEntityManagerFactoryBean();
        emFactory.setDataSource(dataSource());
        emFactory.setPackagesToScan("com.shandakova.documents.entities", "com.shandakova.documents.entities.enums");
        emFactory.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        emFactory.setJpaProperties(jpaProperties());
        return emFactory;
    }

    @Bean
    public PlatformTransactionManager transactionManager() {
        JpaTransactionManager jpaTransactionManager = new JpaTransactionManager();
        jpaTransactionManager.setEntityManagerFactory(entityManagerFactory().getObject());
        return jpaTransactionManager;
    }

    @Bean
    public DataSource dataSource() {
        BasicDataSource source = new BasicDataSource();
        DbProperties dbProperties = dbProperties();
        source.setUrl(dbProperties.getUrl());
        source.setUsername(dbProperties.getUsername());
        source.setPassword(dbProperties.getPassword());
        source.setDriverClassName(dbProperties.getDriver());
        return source;
    }

    public Properties jpaProperties() {
        Properties properties = new Properties();
        properties.setProperty("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
        properties.setProperty("hibernate.connection.release_mode", "after_transaction");
        properties.setProperty("hibernate.format_sql", "true");
        properties.setProperty("hibernate.hbm2ddl.auto", "none");
        return properties;
    }
}
