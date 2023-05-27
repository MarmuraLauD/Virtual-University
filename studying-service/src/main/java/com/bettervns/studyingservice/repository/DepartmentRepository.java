package com.bettervns.studyingservice.repository;

import com.bettervns.studyingservice.models.Department;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DepartmentRepository extends JpaRepository<Department, Integer> {
}