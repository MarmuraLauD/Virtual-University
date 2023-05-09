package com.bettervns.studyingservice.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Year;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Group {
    private int id;
    private String name;
    private Year studyingYear;
    private int departmentId;

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
