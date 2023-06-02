package com.bettervns.studyingservice.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

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

    @OneToMany(mappedBy = "courseToGroup", cascade = CascadeType.ALL)
    private List<CourseAttachedFile> courseAttachedFiles;

    public CourseToGroup(int courseId, int groupId) {
        this.courseId = courseId;
        this.groupId = groupId;
    }

    @Override
    public String toString() {
        return "CourseToGroup{" +
                "id=" + id +
                ", courseId=" + courseId +
                ", groupId=" + groupId +
                ", courseAttachedFiles=" + courseAttachedFiles +
                '}';
    }
}
