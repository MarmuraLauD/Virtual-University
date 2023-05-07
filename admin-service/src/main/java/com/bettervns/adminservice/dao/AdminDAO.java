package com.bettervns.adminservice.dao;

import com.bettervns.adminservice.models.Admin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class AdminDAO {
    private final JdbcTemplate adminsJdbcTemplate;

    @Autowired
    public AdminDAO(JdbcTemplate adminsJdbcTemplate) {
        this.adminsJdbcTemplate = adminsJdbcTemplate;
    }

    public Admin show(int id) {
        return adminsJdbcTemplate.query("SELECT * FROM admin WHERE id=?", new Object[]{id},
                new AdminMapper()).stream().findAny().orElse(null);
    }

}
