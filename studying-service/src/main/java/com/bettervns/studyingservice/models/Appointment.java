package com.bettervns.studyingservice.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Time;
import java.time.LocalDateTime;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Appointment {
    private int id;
    private int teacherId;
    private Time begin_time;
    private Time end_time;
    private Date date;
    private String meetingLink;
    private int recordingId;

    @Override
    public String toString() {
        return "Appointment{" +
                "id=" + id +
                ", teacherId=" + teacherId +
                ", begin_time=" + begin_time +
                ", end_time=" + end_time +
                ", date=" + date +
                ", meetingLink='" + meetingLink + '\'' +
                ", recordingId=" + recordingId +
                '}';
    }
}
