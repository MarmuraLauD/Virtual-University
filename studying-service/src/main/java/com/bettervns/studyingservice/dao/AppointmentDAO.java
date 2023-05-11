package com.bettervns.studyingservice.dao;

import com.bettervns.studyingservice.models.Appointment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AppointmentDAO {

    private final JdbcTemplate coreJdbcTemplate;

    @Autowired
    public AppointmentDAO(JdbcTemplate coreJdbcTemplate) {
        this.coreJdbcTemplate = coreJdbcTemplate;
    }

    public List<Appointment> index() {
        return coreJdbcTemplate.query("SELECT * FROM appointment", new AppointmentMapper());
    }

    public Appointment show(int id) {
        return coreJdbcTemplate.query("SELECT * FROM appointment WHERE id = ?", new Object[]{id},
                new AppointmentMapper()).stream().findAny().orElse(null);
    }

    public void addAppointment(Appointment appointment) {
        coreJdbcTemplate.update("INSERT INTO appointment(teacher_id, begin_time, end_time, date, meeting_link," +
                        " recording_id) VALUES(?, ?, ?, ?, ?, ?)",
                appointment.getTeacherId(), appointment.getBegin_time(), appointment.getEnd_time(),
                appointment.getDate(), appointment.getMeetingLink(), appointment.getRecordingId());
    }

    public void update(int id, Appointment appointment) {
        coreJdbcTemplate.update("UPDATE appointment SET teacher_id = ?, begin_time = ?, end_time = ?, date = ?," +
                        "meeting_link = ?, recording_id = ? WHERE id=?",
                appointment.getTeacherId(), appointment.getBegin_time(), appointment.getEnd_time(),
                appointment.getDate(), appointment.getMeetingLink(), appointment.getRecordingId(), id);
    }

    public void delete(int id) {
        coreJdbcTemplate.update("DELETE FROM appointment WHERE id = ?", id);
    }
}