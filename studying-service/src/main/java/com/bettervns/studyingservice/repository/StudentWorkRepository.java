package com.bettervns.studyingservice.repository;

import com.bettervns.studyingservice.models.StudentWork;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StudentWorkRepository extends JpaRepository<StudentWork, Integer> {
    List<StudentWork> findAllByCourseGroupId(int courseGroupId);

}