package com.EMS.Employee.Management.System.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SampleController {

    @GetMapping("/api/admin/dashboard")
    public String adminDashboard() {
        return "Welcome to Admin Dashboard";
    }

    @GetMapping("/api/user/profile")
    public String userProfile() {
        return "Welcome to User Profile";
    }
}
