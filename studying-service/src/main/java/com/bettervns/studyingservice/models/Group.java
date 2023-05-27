package com.bettervns.studyingservice.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@Entity
@Table(name = "group_table")
public class Group {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "name", nullable = false)
    private String name;
    @Column(name = "studying_year", nullable = false)
    private int studyingYear;

    @Column(name = "department_id")
    private int departmentId;

    public Group(String name, int studyingYear, int departmentId) {
        this.name = name;
        this.studyingYear = studyingYear;
        this.departmentId = departmentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getStudyingYear() {
        return studyingYear;
    }

    public void setStudyingYear(int studyingYear) {
        this.studyingYear = studyingYear;
    }

    public int getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(int departmentId) {
        this.departmentId = departmentId;
    }

    @Override
    public String toString() {
        return "Group{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", studyingYear=" + studyingYear +
                ", departmentId=" + departmentId +
                '}';
    }
}