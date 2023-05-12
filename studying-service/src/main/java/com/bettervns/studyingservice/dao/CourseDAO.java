package com.bettervns.studyingservice.dao;

import com.bettervns.studyingservice.models.Course;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CourseDAO {
    private final JdbcTemplate coreJdbcTemplate;

    @Autowired
    public CourseDAO(JdbcTemplate coreJdbcTemplate) {
        this.coreJdbcTemplate = coreJdbcTemplate;
    }

    public List<Course> index() {
        return coreJdbcTemplate.query("SELECT * FROM course", new CourseMapper());
    }

    public Course show(int id) {
        return coreJdbcTemplate.query("SELECT * FROM course WHERE id = ?", new Object[]{id},
                new CourseMapper()).stream().findAny().orElse(null);
    }

    public void addCourse(Course course) {
        coreJdbcTemplate.update("INSERT INTO course(name, department_id, teacher_id) VALUES(?, ?, ?)",
                course.getName(), course.getDepartmentId(), course.getTeacherId());
    }

    public void update(int id, Course course) {
        coreJdbcTemplate.update("UPDATE course SET name = ?, department_id = ?, teacher_id = ? WHERE id=?",
                course.getName(), course.getDepartmentId(), course.getTeacherId(), id);
    }

    public void delete(int id) {
        coreJdbcTemplate.update("DELETE FROM course WHERE id = ?", id);
    }

}
