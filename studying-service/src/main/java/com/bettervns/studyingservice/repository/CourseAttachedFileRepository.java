package com.bettervns.studyingservice.repository;

import com.bettervns.studyingservice.models.CourseAttachedFile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseAttachedFileRepository extends JpaRepository<CourseAttachedFile, Integer> {
}