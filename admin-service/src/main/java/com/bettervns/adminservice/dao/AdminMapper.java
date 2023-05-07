package com.bettervns.adminservice.dao;

import com.bettervns.adminservice.models.Admin;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class AdminMapper implements RowMapper<Admin> {
    @Override
    public Admin mapRow(ResultSet resultSet, int i) throws SQLException {
        return new Admin(resultSet.getInt("id"), resultSet.getString("admin_name"),
                resultSet.getString("surname"), resultSet.getString("father_name"),
                resultSet.getString("email"));
    }
}