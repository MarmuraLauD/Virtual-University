package com.bettervns.studyingservice.dao;

import com.bettervns.studyingservice.models.StudentAnswer;
import com.bettervns.studyingservice.repository.StudentAnswerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Component
public class StudentAnswerDAO {

    private final StudentAnswerRepository studentAnswerRepository;

    @Autowired
    public StudentAnswerDAO(StudentAnswerRepository studentAnswerRepository) {
        this.studentAnswerRepository = studentAnswerRepository;
    }

    public List<StudentAnswer> getAllStudentAnswers() {
        return studentAnswerRepository.findAll();
    }

    public StudentAnswer getStudentAnswerById(int id) {
        Optional<StudentAnswer> studentAnswers = studentAnswerRepository.findById(id);
        if (studentAnswers.isPresent()) {
            return studentAnswers.get();
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to find resource");
        }
    }

    public List<StudentAnswer> getAllStudentAnswersByStudentWorkId(int studentWorkId) {
        return studentAnswerRepository.findByStudentWorkId(studentWorkId);
    }

    public StudentAnswer getByStudentWorkIdAndStudentId(int studentWorkId, int studentId){
        return studentAnswerRepository.findByStudentWorkIdAndStudentId(studentWorkId, studentId);
    }

    public StudentAnswer add(StudentAnswer studentAnswer) {
        return studentAnswerRepository.save(studentAnswer);
    }

    public void update(int id, StudentAnswer updatedStudentAnswer) {
        Optional<StudentAnswer> optionalStudentAnswer = studentAnswerRepository.findById(id);
        if (optionalStudentAnswer.isPresent()) {
            StudentAnswer studentAnsweri = optionalStudentAnswer.get();
            studentAnsweri.setId(updatedStudentAnswer.getId());
            studentAnsweri.setStudentId(updatedStudentAnswer.getStudentId());
            studentAnsweri.setStudentWorkId(updatedStudentAnswer.getStudentWorkId());
            studentAnsweri.setFileName(updatedStudentAnswer.getFileName());
            studentAnswerRepository.save(studentAnsweri);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to find resource");
        }
    }

    public void delete(int id) {
        studentAnswerRepository.deleteById(id);
    }
}