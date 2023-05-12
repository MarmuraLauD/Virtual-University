package com.bettervns.studentsservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.concurrent.TimeoutException;

@SpringBootApplication
public class StudentsServiceApplication {

    public  static void main(String[] args) throws IOException, TimeoutException {
        SpringApplication.run(StudentsServiceApplication.class, args);
    }

    @Bean
    public DataSource studentsDataSource(){
        DriverManagerDataSource datasource = new DriverManagerDataSource();
        datasource.setDriverClassName("com.mysql.jdbc.Driver");
        datasource.setUrl("jdbc:mysql://localhost:3306/students?serverTimezone=UTC&useSSL=false&allowPublicKeyRetrieval=true");
        datasource.setUsername("danyil");
        datasource.setPassword("Grisha_sobaka1");
        return datasource;
    }

    @Bean
    public JdbcTemplate studentsJdbcTemplate(){
        return new JdbcTemplate(studentsDataSource());
    }
}