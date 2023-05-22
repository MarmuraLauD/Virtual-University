package com.bettervns.teachersservice.repository;

import com.bettervns.teachersservice.models.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeacherRepository extends JpaRepository <Teacher, Integer> {
}
