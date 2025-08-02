
package com.EMS.Employee.Management.System.controller;

import com.EMS.Employee.Management.System.dto.EmployeeDTO;
import com.EMS.Employee.Management.System.dto.RoleDTO;
import com.EMS.Employee.Management.System.dto.UserDTO;
import com.EMS.Employee.Management.System.dto.VerifyUserRequestDTO;
import com.EMS.Employee.Management.System.entity.User;
import com.EMS.Employee.Management.System.service.AuthenticationService;
import com.EMS.Employee.Management.System.service.EmployeeService;
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
    private final EmployeeService employeeService = null;

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

    @GetMapping("/admins/all")
    public ResponseEntity<List<UserDTO>> getAllAdmins() {
        try {
            return ResponseEntity.ok(userService.getAllAdmins());
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
            }
        }
    

    @PutMapping("/employee/{id}/verify")
    public ResponseEntity<UserDTO> verifyEmployee(@PathVariable Long id, @RequestBody VerifyUserRequestDTO verifyDTO) {
        try {
            UserDTO updatedUser = userService.verifyAndUpdateUserRoleAndProfile(
                    id,
                    verifyDTO.getRole()
            );
            User user = userService.getUserEntityById(id);
            authenticationService.sendVerificationEmail(user, verifyDTO.getRole(), verifyDTO.getDesignation(), verifyDTO.getDepartment());
            return ResponseEntity.ok(updatedUser);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    // @PutMapping("/employee/{id}/profile")
    // public ResponseEntity<EmployeeDTO> updateEmployeeProfile(
    //         @PathVariable int id,
    //         @RequestBody EmployeeDTO employeeDTO) {
    //     return employeeService.updateProfileFields(id, employeeDTO);
    // }

    @PutMapping("/verify-all")
    public ResponseEntity<List<UserDTO>> verifyAllEmployees(@RequestBody RoleDTO roleDTO) {
        try {
            List<UserDTO> verifiedUsers = userService.verifyAllUnverifiedEmployees(roleDTO.role());
            verifiedUsers.forEach(user -> authenticationService.sendVerificationEmail(
                    userService.getUserEntityById(user.getId()), roleDTO.role(), null, null));
            return ResponseEntity.ok(verifiedUsers);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }
}
