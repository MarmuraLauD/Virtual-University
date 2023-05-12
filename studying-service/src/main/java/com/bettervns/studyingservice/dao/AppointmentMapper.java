package com.bettervns.studyingservice.dao;

import com.bettervns.studyingservice.models.Appointment;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class AppointmentMapper implements RowMapper<Appointment> {

    @Override
    public Appointment mapRow(ResultSet resultSet, int i) throws SQLException {
        return new Appointment(resultSet.getInt("id"), resultSet.getInt("teacher_id"),
                resultSet.getTime("begin_time"), resultSet.getTime("end_time"),
                resultSet.getDate("date"), resultSet.getString("meeting_link"),
                resultSet.getInt("recording_id"));
    }


}
