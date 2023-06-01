package com.bettervns.studyingservice.repository;

import com.bettervns.studyingservice.models.StudentToCourseGroup;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentToCourseGroupRepository extends JpaRepository<StudentToCourseGroup, Integer> {
}