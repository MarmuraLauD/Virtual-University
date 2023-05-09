package com.bettervns.studyingservice;

import com.bettervns.studyingservice.models.Department;
import com.bettervns.studyingservice.models.Group;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;
import java.time.Year;

@SpringBootApplication
public class StudyingServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(StudyingServiceApplication.class, args);
        Department dept = new Department(1, "name", "phone", "email");
        Group group = new Group(1, "KI-44", Year.of(4), 1);
        System.out.println(dept.toString() + "\n" + group.toString());
    }

    @Bean
    public DataSource coreDataSource(){
        DriverManagerDataSource datasource = new DriverManagerDataSource();
        datasource.setDriverClassName("com.mysql.jdbc.Driver");
        datasource.setUrl("jdbc:mysql://localhost:3306/core?serverTimezone=UTC&useSSL=false&allowPublicKeyRetrieval=true");
        datasource.setUsername("danyil");
        datasource.setPassword("Grisha_sobaka1");
        return datasource;
    }

    @Bean
    public JdbcTemplate coreJdbcTemplate(){
        return new JdbcTemplate(coreDataSource());
    }
}