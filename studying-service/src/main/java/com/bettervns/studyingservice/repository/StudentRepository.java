package com.bettervns.studyingservice.repository;

import com.bettervns.studyingservice.models.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<Student, Integer> {
}
