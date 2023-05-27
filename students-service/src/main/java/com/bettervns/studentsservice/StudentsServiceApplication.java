package com.bettervns.studentsservice;

import com.bettervns.studyingservice.dao.StudentDAO;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

@SpringBootApplication
public class StudentsServiceApplication {

    public  static void main(String[] args) throws IOException, TimeoutException {
        SpringApplication.run(StudentsServiceApplication.class, args);
    }
}