package com.bettervns.studentsservice.dao;

import com.bettervns.studentsservice.models.Student;
import com.bettervns.studentsservice.repository.StudentRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Component
public class StudentDAO {
    private final StudentRepository studentRepository;

    @Autowired
    public StudentDAO(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    public Student getStudentById(int id) {
        Optional<Student> students = studentRepository.findById(id);
        if (students.isPresent()) {
            return students.get();
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to find resource");
        }
    }

    public Student getStudentByEmail(String email){
        return studentRepository.findByEmail(email);
    }

    public Student add(Student student) {
        return studentRepository.save(student);
    }

    public void update(int id, Student updatedStudent) {
        Optional<Student> optionalStudent = studentRepository.findById(id);
        if (optionalStudent.isPresent()) {
            Student studenti = optionalStudent.get();
            studenti.setName(updatedStudent.getName());
            studenti.setSurname(updatedStudent.getSurname());
            studenti.setFather(updatedStudent.getFather());
            studenti.setDate(updatedStudent.getDate());
            studenti.setEmail(updatedStudent.getEmail());
            studenti.setGroupId(updatedStudent.getGroupId());
            studentRepository.save(studenti);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to find resource");
        }
    }

    public void delete(int id) {
        studentRepository.deleteById(id);
    }
}