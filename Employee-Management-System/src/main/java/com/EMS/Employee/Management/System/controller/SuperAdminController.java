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
@PreAuthorize("hasRole('SUPER_ADMIN')")
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
    public ResponseEntity<UserDTO> verifyEmployee(@PathVariable Long id, @RequestBody String role) {
        UserDTO updatedUser = userService.updateUserRole(id, role);
        authenticationService.sendVerificationEmail(userService.getUserEntityById(id), role);
        return ResponseEntity.ok(updatedUser);
    }

    @PutMapping("/verify-all")
    public ResponseEntity<List<UserDTO>> verifyAllEmployees(@RequestBody String role) {
        List<UserDTO> verifiedUsers = userService.verifyAllUnverifiedEmployees(role);
        verifiedUsers.forEach(user -> authenticationService.sendVerificationEmail(
                userService.getUserEntityById(user.getId()), role));
        return ResponseEntity.ok(verifiedUsers);
    }
}