package com.bettervns.adminservice.controllers;

import com.bettervns.adminservice.dao.AdminDAO;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/admin")
public class AdminsController {

    private final AdminDAO adminDao;

    public AdminsController(AdminDAO adminDao) {
        this.adminDao = adminDao;
    }

    @GetMapping("/{id}")
    public String homePage(@PathVariable("id") int id, Model model) {
        model.addAttribute("admin", adminDao.show(id));
        return adminDao.show(id).toString();
    }
}