package com.bettervns.studyingservice.repository;

import com.bettervns.studyingservice.models.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppointmentRepository  extends JpaRepository<Appointment, Integer> {
}