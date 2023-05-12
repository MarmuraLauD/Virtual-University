package com.bettervns.studyingservice.dao;

import com.bettervns.studyingservice.models.Course;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CourseMapper implements RowMapper<Course> {

    @Override
    public Course mapRow(ResultSet resultSet, int i) throws SQLException {
        return new Course(resultSet.getInt("id"), resultSet.getString("name"),
                resultSet.getInt("department_id"), resultSet.getInt("teacher_id") );
    }

}
