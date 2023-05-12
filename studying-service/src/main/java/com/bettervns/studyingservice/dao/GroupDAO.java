package com.bettervns.studyingservice.dao;

import com.bettervns.studyingservice.models.Department;
import com.bettervns.studyingservice.models.Group;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class GroupDAO {

    private final JdbcTemplate coreJdbcTemplate;

    @Autowired
    public GroupDAO(JdbcTemplate coreJdbcTemplate) {
        this.coreJdbcTemplate = coreJdbcTemplate;
    }

    public List<Group> index() {
        return coreJdbcTemplate.query("SELECT * FROM students_group ", new GroupMapper());
    }

    public Group show(int id) {
        return coreJdbcTemplate.query("SELECT * FROM students_group WHERE id = ?", new Object[]{id},
                new GroupMapper()).stream().findAny().orElse(null);
    }

    public void addGroup(Group group) {
        coreJdbcTemplate.update("INSERT INTO students_group(name, studying_year, department_id) VALUES(?, ?, ?)",
                group.getName(), group.getStudyingYear(), group.getDepartmentId());
    }

    public void update(int id, Group group) {
        coreJdbcTemplate.update("UPDATE students_group SET name = ?, studying_year = ?, department_id = ? WHERE id=?",
                group.getName(), group.getStudyingYear(), group.getDepartmentId(), id);
    }

    public void delete(int id) {
        coreJdbcTemplate.update("DELETE FROM students_group WHERE id = ?", id);
    }
}