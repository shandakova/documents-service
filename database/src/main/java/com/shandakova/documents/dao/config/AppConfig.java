package com.shandakova.documents.dao.config;

import com.shandakova.documents.ConnectionPool;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.sql.SQLException;

@Configuration
@ComponentScan(basePackages = "com.shandakova.documents")
@PropertySource("classpath:database.properties")
public class AppConfig {
    @Bean
    public DbProperties dbProperties() {
        return new DbProperties();
    }

    @Bean
    public ConnectionPool connectionPool() throws SQLException {
        return new ConnectionPool(dbProperties());
    }
}
