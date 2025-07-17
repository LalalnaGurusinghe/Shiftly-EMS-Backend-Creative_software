package com.EMS.Employee.Management.System.controller;

import com.EMS.Employee.Management.System.dto.RoleDTO;
import com.EMS.Employee.Management.System.dto.UserDTO;
import com.EMS.Employee.Management.System.entity.User;
import com.EMS.Employee.Management.System.service.AuthenticationService;
import com.EMS.Employee.Management.System.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/superadmin")
@PreAuthorize("hasRole('ROLE_SUPER_ADMIN')")
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
        try {
            return ResponseEntity.ok(userService.getAllUsers());
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    @PutMapping("/employee/{id}/verify")
    public ResponseEntity<UserDTO> verifyEmployee(@PathVariable Long id, @RequestBody RoleDTO roleDTO) {
        try {
            UserDTO updatedUser = userService.verifyAndUpdateUserRole(id, roleDTO.role());
            User user = userService.getUserEntityById(id);
            authenticationService.sendVerificationEmail(user, roleDTO.role());
            return ResponseEntity.ok(updatedUser);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    @PutMapping("/verify-all")
    public ResponseEntity<List<UserDTO>> verifyAllEmployees(@RequestBody RoleDTO roleDTO) {
        try {
            List<UserDTO> verifiedUsers = userService.verifyAllUnverifiedEmployees(roleDTO.role());
            verifiedUsers.forEach(user -> authenticationService.sendVerificationEmail(
                    userService.getUserEntityById(user.getId()), roleDTO.role()));
            return ResponseEntity.ok(verifiedUsers);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }
}