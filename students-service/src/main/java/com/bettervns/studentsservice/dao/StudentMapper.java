package com.bettervns.studentsservice.dao;

import com.bettervns.studentsservice.models.Student;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class StudentMapper implements RowMapper<Student> {
    @Override
    public Student mapRow(ResultSet resultSet, int i) throws SQLException {
        return new Student(resultSet.getInt("id"), resultSet.getString("student_name"),
                resultSet.getString("surname"), resultSet.getString("father_name"),
                resultSet.getDate("admission_date"), resultSet.getString("email"),
                resultSet.getInt("group_id"));
    }
}