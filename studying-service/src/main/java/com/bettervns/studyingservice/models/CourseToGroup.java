package com.bettervns.studyingservice.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "course_group")
public class CourseToGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "course_id", nullable = false)
    private int courseId;
    @Column(name = "group_id", nullable = false)
    private int groupId;


    public CourseToGroup(int courseId, int groupId) {
        this.courseId = courseId;
        this.groupId = groupId;
    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    @Override
    public String toString() {
        return "CourseToGroup{" +
                "id=" + id +
                ", courseId=" + courseId +
                ", groupId=" + groupId +
                '}';
    }
}
