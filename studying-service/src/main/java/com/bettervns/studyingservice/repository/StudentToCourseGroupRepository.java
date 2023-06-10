package com.bettervns.studyingservice.repository;

import com.bettervns.studyingservice.models.StudentToCourseGroup;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StudentToCourseGroupRepository extends JpaRepository<StudentToCourseGroup, Integer> {
    List<StudentToCourseGroup> findByCourseGroupId(int courseGroupId);
}