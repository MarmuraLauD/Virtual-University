package com.bettervns.studyingservice.repository;

import com.bettervns.studyingservice.models.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
public interface TeacherRepository extends JpaRepository <Teacher, Integer> {
}
