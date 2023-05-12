package com.bettervns.studyingservice.dao;

import com.bettervns.studyingservice.models.Group;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class GroupMapper implements RowMapper<Group> {

    @Override
    public Group mapRow(ResultSet resultSet, int i) throws SQLException {
        return new Group(resultSet.getInt("id"), resultSet.getString("name"),
                resultSet.getInt("studying_year"), resultSet.getInt("department_id") );
    }

}
