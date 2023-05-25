package com.bettervns.adminservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

@SpringBootApplication
public class AdminServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(AdminServiceApplication.class, args);
    }

    @Bean
    public DataSource adminsDataSource(){
        DriverManagerDataSource datasource = new DriverManagerDataSource();
        datasource.setDriverClassName("com.mysql.jdbc.Driver");
        datasource.setUrl("jdbc:mysql://localhost:3306/admins?serverTimezone=UTC&useSSL=false&allowPublicKeyRetrieval=true");
        datasource.setUsername("root");
        datasource.setPassword("Vladmarmura2002");
        return datasource;
    }

    @Bean
    public JdbcTemplate adminsJdbcTemplate(){
        return new JdbcTemplate(adminsDataSource());
    }
}