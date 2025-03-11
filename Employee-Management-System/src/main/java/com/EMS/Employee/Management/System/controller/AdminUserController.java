package com.EMS.Employee.Management.System.controller;

import com.EMS.Employee.Management.System.dto.AdminUserDTO;
import com.EMS.Employee.Management.System.service.AdminUserService;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<AdminUserDTO> addAdminUser(@RequestBody AdminUserDTO adminUserDTO) {
        return adminUserService.addUser(adminUserDTO);
    }

    @GetMapping("/all")
    public List<AdminUserDTO> getAllAdminUsers() {
        return adminUserService.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<AdminUserDTO> getAdminUserById(@PathVariable int id) {
        return adminUserService.getUserById(id);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<AdminUserDTO> deleteAdminUserById(@PathVariable int id) {
        return adminUserService.deleteUserById(id);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<AdminUserDTO> updateAdminUserById(@PathVariable int id, @RequestBody AdminUserDTO adminUserDTO) {
        return adminUserService.updateUserById(id, adminUserDTO);
    }
}
