package com.bettervns.studyingservice.dao;

import com.bettervns.studyingservice.models.StudentToCourseGroup;
import com.bettervns.studyingservice.repository.StudentToCourseGroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Component
public class StudentToCourseGroupDAO {

    private final StudentToCourseGroupRepository StudentToCourseGroupRepository;

    @Autowired
    public StudentToCourseGroupDAO(StudentToCourseGroupRepository StudentToCourseGroupRepository) {
        this.StudentToCourseGroupRepository = StudentToCourseGroupRepository;
    }

    public List<StudentToCourseGroup> getAllStudentToCourseGroups() {
        return StudentToCourseGroupRepository.findAll();
    }

    public StudentToCourseGroup getStudentToCourseGroupById(int id) {
        Optional<StudentToCourseGroup> studentToCourseGroups = StudentToCourseGroupRepository.findById(id);
        if (studentToCourseGroups.isPresent()) {
            return studentToCourseGroups.get();
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to find resource");
        }
    }

    public StudentToCourseGroup add(StudentToCourseGroup studentToCourseGroup) {
        return StudentToCourseGroupRepository.save(studentToCourseGroup);
    }

    public void update(int id, StudentToCourseGroup updatedStudentToCourseGroup) {
        Optional<StudentToCourseGroup> optionalStudentToCourseGroup = StudentToCourseGroupRepository.findById(id);
        if (optionalStudentToCourseGroup.isPresent()) {
            StudentToCourseGroup studentToCourseGroupi = optionalStudentToCourseGroup.get();
            studentToCourseGroupi.setStudentId(updatedStudentToCourseGroup.getStudentId());
            studentToCourseGroupi.setCourseGroupId(updatedStudentToCourseGroup.getCourseGroupId());
            StudentToCourseGroupRepository.save(studentToCourseGroupi);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to find resource");
        }
    }

    public void delete(int id) {
        StudentToCourseGroupRepository.deleteById(id);
    }
}