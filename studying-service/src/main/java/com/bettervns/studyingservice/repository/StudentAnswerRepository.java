package com.bettervns.studyingservice.repository;

import com.bettervns.studyingservice.models.StudentAnswer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StudentAnswerRepository extends JpaRepository<StudentAnswer, Integer> {

    List<StudentAnswer> findByStudentWorkId(int studentWorkId);

    StudentAnswer findByStudentWorkIdAndStudentId(int studentWorkId, int studentId);

}