package com.bettervns.studyingservice.dao;

import com.bettervns.studyingservice.models.Teacher;
import com.bettervns.studyingservice.repository.TeacherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Repository
public class TeacherDAO {

    private final TeacherRepository teacherRepository;

    @Autowired
    public TeacherDAO(TeacherRepository teacherRepository) {
        this.teacherRepository = teacherRepository;
    }

    public List<Teacher> getAllTeachers() {
        return teacherRepository.findAll();
    }

    public Teacher getTeacherById(int id) {
        Optional<Teacher> teacher = teacherRepository.findById(id);
        if (teacher.isPresent()) {
            return teacher.get();
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to find resource");
        }
    }

    public Teacher add(Teacher teacher) {
        return teacherRepository.save(teacher);
    }

    public void update(int id, Teacher updatedTeacher) {
        Optional<Teacher> optionalTeacher = teacherRepository.findById(id);
        if (optionalTeacher.isPresent()) {
            Teacher teacher = optionalTeacher.get();
            teacher.setName(updatedTeacher.getName());
            teacher.setSurname(updatedTeacher.getSurname());
            teacher.setFather(updatedTeacher.getFather());
            teacher.setEmail(updatedTeacher.getEmail());
            teacher.setDepartment(updatedTeacher.getDepartment());
            teacherRepository.save(teacher);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to find resource");
        }
    }

    public void delete(int id) {
        teacherRepository.deleteById(id);
    }
}