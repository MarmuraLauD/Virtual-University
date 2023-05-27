package com.bettervns.studyingservice.repository;

import com.bettervns.studyingservice.models.Group;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GroupRepository extends JpaRepository<Group, Integer> {
}