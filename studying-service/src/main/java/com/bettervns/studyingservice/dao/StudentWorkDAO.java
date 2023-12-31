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

    public final StudentWorkRepository studentWorkRepository;

    @Autowired
    public StudentWorkDAO(StudentWorkRepository studentWorkRepository) {
        this.studentWorkRepository = studentWorkRepository;
    }

    public List<StudentWork> getAllStudentWorks() {
        return studentWorkRepository.findAll();
    }

    public StudentWork getStudentWorkById(int id) {
        Optional<StudentWork> studentWorks = studentWorkRepository.findById(id);
        if (studentWorks.isPresent()) {
            return studentWorks.get();
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to find resource");
        }
    }

    public List<StudentWork> getAllStudentWorksByCourseGroupId(int courseGroupId) {
        return studentWorkRepository.findAllByCourseGroupId(courseGroupId);
    }

    public StudentWork add(StudentWork studentWork) {
        return studentWorkRepository.save(studentWork);
    }

    public void update(int id, StudentWork updatedStudentWork) {
        Optional<StudentWork> optionalStudentWork = studentWorkRepository.findById(id);
        if (optionalStudentWork.isPresent()) {
            StudentWork studentWorki = optionalStudentWork.get();
            studentWorki.setId(updatedStudentWork.getId());
            studentWorki.setName(updatedStudentWork.getName());
            studentWorki.setDescription(updatedStudentWork.getDescription());
            studentWorki.setCourseGroupId(updatedStudentWork.getCourseGroupId());
            studentWorkRepository.save(studentWorki);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to find resource");
        }
    }

    public void delete(int id) {
        studentWorkRepository.deleteById(id);
    }
}