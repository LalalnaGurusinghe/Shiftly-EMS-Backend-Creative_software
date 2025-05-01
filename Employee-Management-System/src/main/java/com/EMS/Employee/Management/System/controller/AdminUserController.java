package com.EMS.Employee.Management.System.controller;

import com.EMS.Employee.Management.System.dto.AdminUserDTO;
import com.EMS.Employee.Management.System.service.AdminUserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/shiftly/ems/admin/user")
@CrossOrigin
public class AdminUserController {

    private final AdminUserService adminUserService;

    public AdminUserController(AdminUserService adminUserService) {
        this.adminUserService = adminUserService;
    }

    @PostMapping("/add")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AdminUserDTO> addAdminUser(@Valid @RequestBody AdminUserDTO adminUserDTO) {
        return adminUserService.addUser(adminUserDTO);
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public List<AdminUserDTO> getAllAdminUsers() {
        return adminUserService.getAll();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AdminUserDTO> getAdminUserById(@PathVariable int id) {
        return adminUserService.getUserById(id);
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AdminUserDTO> deleteAdminUserById(@PathVariable int id) {
        return adminUserService.deleteUserById(id);
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AdminUserDTO> updateAdminUserById(@PathVariable int id, @Valid @RequestBody AdminUserDTO adminUserDTO) {
        return adminUserService.updateUserById(id, adminUserDTO);
    }
}