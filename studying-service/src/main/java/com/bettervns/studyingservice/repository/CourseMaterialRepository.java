package com.bettervns.studyingservice.repository;

import com.bettervns.studyingservice.models.CourseMaterial;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CourseMaterialRepository extends JpaRepository<CourseMaterial, Integer> {
    List<CourseMaterial> findByFileName(String fileName);
}