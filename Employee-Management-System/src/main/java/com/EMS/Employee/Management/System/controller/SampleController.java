package com.EMS.Employee.Management.System.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SampleController {
    @GetMapping("/api/admin/dashboard")
    @PreAuthorize("hasRole('ADMIN')")
    public String adminDashboard() {
        return "Welcome to Admin Dashboard";
    }

    @GetMapping("/api/user/profile")
    @PreAuthorize("hasRole('USER')")
    public String userProfile() {
        return "Welcome to User Profile";
    }
}