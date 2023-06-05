package com.bettervns.studyingservice.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name = "course_for_group_attached_files")
@NoArgsConstructor
public class CourseAttachedFile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "name", nullable = false)
    private int name;
    @Column(name = "file_link", nullable = false)
    private String fileLink;
    @Column
    private int courseToGroupId;

    public CourseAttachedFile(int name, String fileLink, int courseToGroupId) {
        this.name = name;
        this.fileLink = fileLink;
        this.courseToGroupId = courseToGroupId;
    }

    @Override
    public String toString() {
        return "CourseAttachedFile{" +
                "id=" + id +
                ", name=" + name +
                ", fileLink='" + fileLink + '\'' +
                ", courseToGroup id=" + courseToGroupId +
                '}';
    }
}