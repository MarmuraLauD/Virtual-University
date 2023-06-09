package com.bettervns.studyingservice.repository;

import com.bettervns.studyingservice.models.CourseMaterial;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseAttachedFileRepository extends JpaRepository<CourseMaterial, Integer> {
}