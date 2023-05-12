package com.bettervns.studentsservice.dao;

import com.bettervns.studentsservice.models.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class StudentDAO {
    private final JdbcTemplate studentsJdbcTemplate;

    @Autowired
    public StudentDAO(JdbcTemplate studentsJdbcTemplate){
        this.studentsJdbcTemplate = studentsJdbcTemplate;
    }

    public List<Student> index() {
        return studentsJdbcTemplate.query("SELECT * FROM Student", new StudentMapper());
    }

    /*public List<Student> showOfGroup(int groupId) {
        return studentsJdbcTemplate.query("SELECT * FROM Student WHERE group_id = ?", new Object[]{groupId}, new StudentMapper());
    }*/

    public Student show(int id) {
        return studentsJdbcTemplate.query("SELECT * FROM student WHERE id = ?", new Object[]{id},
                new StudentMapper()).stream().findAny().orElse(null);
    }

    public void addStudent(Student student) {
        studentsJdbcTemplate.update("INSERT INTO student(name, surname, father, date, email, group_id) " +
                        "VALUES(?, ?, ?, ?, ?, ?)",
                student.getName(), student.getSurname(), student.getFather(), student.getDate(), student.getEmail(), student.getGroupId());
    }

    public void update(int id, Student student) {
        studentsJdbcTemplate.update("UPDATE student SET name = ?, surname = ?, father = ?, date = ?, email = ?, group_id = ? WHERE id=?",
                student.getName(), student.getSurname(), student.getFather(), student.getDate(), student.getEmail(), student.getGroupId(), id);
    }

    public void delete(int id) {
        studentsJdbcTemplate.update("DELETE FROM student WHERE id = ?", id);
    }
}