package com.bettervns.studyingservice.dao;

import com.bettervns.studyingservice.models.StudentWork;
import com.bettervns.studyingservice.repository.StudentWorkRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Component
public class StudentWorkDAO {

    private final StudentWorkRepository StudentWorkRepository;

    @Autowired
    public StudentWorkDAO(StudentWorkRepository StudentWorkRepository) {
        this.StudentWorkRepository = StudentWorkRepository;
    }

    public List<StudentWork> getAllStudentWorks() {
        return StudentWorkRepository.findAll();
    }

    public StudentWork getStudentWorkById(int id) {
        Optional<StudentWork> studentWorks = StudentWorkRepository.findById(id);
        if (studentWorks.isPresent()) {
            return studentWorks.get();
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to find resource");
        }
    }

    public StudentWork add(StudentWork studentWork) {
        return StudentWorkRepository.save(studentWork);
    }

    public void update(int id, StudentWork updatedStudentWork) {
        Optional<StudentWork> optionalStudentWork = StudentWorkRepository.findById(id);
        if (optionalStudentWork.isPresent()) {
            StudentWork studentWorki = optionalStudentWork.get();
            studentWorki.setName(updatedStudentWork.getName());
            studentWorki.setStudentId(updatedStudentWork.getStudentId());
            studentWorki.setMark(updatedStudentWork.getMark());
            studentWorki.setFileLink(updatedStudentWork.getFileLink());
            studentWorki.setStudentToCourseGroupId(updatedStudentWork.getStudentToCourseGroupId());
            StudentWorkRepository.save(studentWorki);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to find resource");
        }
    }

    public void delete(int id) {
        StudentWorkRepository.deleteById(id);
    }
}