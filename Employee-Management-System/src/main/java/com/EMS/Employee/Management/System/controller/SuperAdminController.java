package com.EMS.Employee.Management.System.controller;

import com.EMS.Employee.Management.System.dto.UserDTO;
import com.EMS.Employee.Management.System.service.AuthenticationService;
import com.EMS.Employee.Management.System.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/superadmin")
@PreAuthorize("hasRole('ROLE_SUPER_ADMIN')") // Updated to match ROLE_ prefix
@CrossOrigin(origins = "http://localhost:3000")
public class SuperAdminController {
    private final UserService userService;
    private final AuthenticationService authenticationService;

    public SuperAdminController(UserService userService, AuthenticationService authenticationService) {
        this.userService = userService;
        this.authenticationService = authenticationService;
    }

    @GetMapping("/employees")
    public ResponseEntity<List<UserDTO>> getAllEmployees() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @PutMapping("/employee/{id}/verify")
    public ResponseEntity<UserDTO> verifyEmployee(@PathVariable Long id, @RequestBody RoleDTO roleDTO) {
        UserDTO updatedUser = userService.updateUserRole(id, roleDTO.role());
        authenticationService.sendVerificationEmail(userService.getUserEntityById(id), roleDTO.role());
        return ResponseEntity.ok(updatedUser);
    }

    @PutMapping("/verify-all")
    public ResponseEntity<List<UserDTO>> verifyAllEmployees(@RequestBody RoleDTO roleDTO) {
        List<UserDTO> verifiedUsers = userService.verifyAllUnverifiedEmployees(roleDTO.role());
        verifiedUsers.forEach(user -> authenticationService.sendVerificationEmail(
                userService.getUserEntityById(user.getId()), roleDTO.role()));
        return ResponseEntity.ok(verifiedUsers);
    }
}

record RoleDTO(String role) {}