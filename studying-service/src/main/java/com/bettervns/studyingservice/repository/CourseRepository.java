package com.bettervns.studyingservice.repository;

import com.bettervns.studyingservice.models.Course;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseRepository extends JpaRepository<Course, Integer> {
}
