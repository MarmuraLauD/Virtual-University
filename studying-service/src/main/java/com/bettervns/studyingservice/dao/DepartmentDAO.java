package com.bettervns.studyingservice.dao;

import com.bettervns.studyingservice.models.Department;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DepartmentDAO {
    private final JdbcTemplate coreJdbcTemplate;

    @Autowired
    public DepartmentDAO(JdbcTemplate coreJdbcTemplate) {
        this.coreJdbcTemplate = coreJdbcTemplate;
    }

    public List<Department> index() {
        return coreJdbcTemplate.query("SELECT * FROM Student", new DepartmentMapper());
    }

    public void addDepartment(Department department) {
        coreJdbcTemplate.update("INSERT INTO department(name, phone, email) VALUES(?, ?, ?)",
                department.getName(), department.getPhone(), department.getEmail());
    }

    public void update(int id, Department department) {
        coreJdbcTemplate.update("UPDATE department SET name = ?, phone = ?, email = ? WHERE id=?",
                department.getName(), department.getPhone(), department.getEmail(), id);
    }

    public void delete(int id) {
        coreJdbcTemplate.update("DELETE FROM department WHERE id = ?", id);
    }
}