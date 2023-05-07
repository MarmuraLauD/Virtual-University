package com.bettervns.controllers;

import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@CrossOrigin(origins = "*", maxAge = 3600)
@Controller
@RequestMapping("/")
public class LoginController {

    @GetMapping("/signin")
    public String getLoginPage() {
        return "login";
    }

    @GetMapping("/home")
    public String getHomePage() {
        return "index";
    }

}
