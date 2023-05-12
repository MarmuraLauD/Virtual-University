package com.bettervns.studyingservice.dao;

import com.bettervns.studyingservice.models.Department;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class DepartmentMapper implements RowMapper<Department> {

    @Override
    public Department mapRow(ResultSet resultSet, int i) throws SQLException {
        return new Department(resultSet.getInt("id"), resultSet.getString("name"),
                resultSet.getString("phone"), resultSet.getString("email") );
    }
}