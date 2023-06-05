package com.bettervns.studyingservice;

import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.sql.Date;
import java.time.LocalDate;

@SpringBootApplication
public class StudyingServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(StudyingServiceApplication.class, args);
        System.out.println(Date.valueOf("2020-2-2").compareTo(Date.valueOf("2019-2-2")) >= 0);
        System.out.println(Date.valueOf("2020-2-2").compareTo(Date.valueOf("2019-2-2")) <= 0);
        System.out.println(Date.valueOf("2020-2-2").compareTo(Date.valueOf("2020-2-2")) <= 0);
    }
    @Bean
    public ConnectionFactory connectionFactory() {
        CachingConnectionFactory factory = new CachingConnectionFactory("localhost");
        factory.setUsername("bettervns");
        factory.setPassword("bettervns");
        return factory;
    }
}