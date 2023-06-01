package com.bettervns.studyingservice.repository;

import com.bettervns.studyingservice.models.AppointmentToGroup;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppointmentToGroupRepository extends JpaRepository<AppointmentToGroup, Integer> {
}