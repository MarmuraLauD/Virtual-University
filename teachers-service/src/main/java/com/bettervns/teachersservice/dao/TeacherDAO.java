package com.bettervns.teachersservice.dao;


import com.bettervns.teachersservice.models.Teacher;
import com.bettervns.teachersservice.repository.TeacherRepository;
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

//    public List<Teacher> index() {
//        return teachersJdbcTemplate.query("SELECT * FROM teacher", new com.bettervns.teacherservice.dao.TeacherMapper());
//    }

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

//    public Teacher show(int id) {
//        return teachersJdbcTemplate.query("SELECT * FROM teacher WHERE id=?", new Object[]{id},
//                new com.bettervns.teacherservice.dao.TeacherMapper()).stream().findAny().orElse(null);
//    }

    public Teacher addTeacher(Teacher teacher) {
        return teacherRepository.save(teacher);
    }


//    public void update(int id, Teacher teacher) {
//        teachersJdbcTemplate.update("UPDATE teacher SET teacher_name=?,surname=?,father_name=?, email=?, chair_id=? WHERE id=?",
//                teacher.getName(), teacher.getSurname(), teacher.getFather_name(), teacher.getEmail(), teacher.getChair_id(), id);
//    }

//    public void update(int id, Teacher updatedTeacher) {
//        teacherRepository.findById(id).ifPresent(teacher -> {
//            teacher.setName(updatedTeacher.getName());
//            teacher.setSurname(updatedTeacher.getSurname());
//            teacher.setFather_name(updatedTeacher.getFather_name());
//            teacher.setEmail(updatedTeacher.getEmail());
//            teacher.setChair_id(updatedTeacher.getChair_id());
//            teacherRepository.save(teacher);
//        });
//    }

    public void update(int id, Teacher updatedTeacher) {
        Optional<Teacher> optionalTeacher = teacherRepository.findById(id);
        if (optionalTeacher.isPresent()) {
            Teacher teacher = optionalTeacher.get();
            teacher.setName(updatedTeacher.getName());
            teacher.setSurname(updatedTeacher.getSurname());
            teacher.setFather(updatedTeacher.getFather());
            teacher.setEmail(updatedTeacher.getEmail());
            teacher.setChair_id(updatedTeacher.getChair_id());
            teacherRepository.save(teacher);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to find resource");
        }
    }


//    public void delete(int id) {
//        teachersJdbcTemplate.update("DELETE FROM teacher WHERE id = ?", id);
//    }

    public void deleteTeacher(int id) {
        teacherRepository.deleteById(id);
    }
}
