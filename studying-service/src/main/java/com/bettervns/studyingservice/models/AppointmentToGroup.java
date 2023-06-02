package com.bettervns.studyingservice.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "appointment_group")
public class AppointmentToGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "appointment_id", nullable = false)
    private int appointmentId;
    @Column(name = "group_id", nullable = false)
    private int groupId;

    public AppointmentToGroup(int appointmentId, int groupId) {
        this.appointmentId = appointmentId;
        this.groupId = groupId;
    }

    public int getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(int appointmentId) {
        this.appointmentId = appointmentId;
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    @Override
    public String toString() {
        return "AppointmentToGroup{" +
                "id=" + id +
                ", appointmentId=" + appointmentId +
                ", groupId=" + groupId +
                '}';
    }
}
