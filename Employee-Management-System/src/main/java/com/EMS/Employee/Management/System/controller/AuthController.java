package com.EMS.Employee.Management.System.controller;

import com.EMS.Employee.Management.System.dto.*;
import com.EMS.Employee.Management.System.entity.User;
import com.EMS.Employee.Management.System.repo.UserRepo;
import com.EMS.Employee.Management.System.service.AuthenticationService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:3000")
public class AuthController {
    private final AuthenticationService authenticationService;
    private final UserRepo userRepo;

    public AuthController(AuthenticationService authenticationService, UserRepo userRepo) {
        this.authenticationService = authenticationService;
        this.userRepo = userRepo;
    }

    @PostMapping("/register")
    public ResponseEntity<UserDTO> registerEmployee(@Valid @RequestBody RegisterRequestDTO registerRequestDTO) {
        return ResponseEntity.ok(authenticationService.registerEmployee(registerRequestDTO));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@Valid @RequestBody LoginRequestDTO loginRequestDTO) {
        LoginResponseDTO loginResponseDTO = authenticationService.login(loginRequestDTO);
        return ResponseEntity.ok(loginResponseDTO);
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout() {
        return ResponseEntity.ok("Logged out successfully");
    }

    @GetMapping("/currentuser")
    public ResponseEntity<?> getCurrentUser(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(401).body("Unauthorized");
        }
        String username = authentication.getName();
        User user = userRepo.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return ResponseEntity.ok(convertToUserDTO(user));
    }

    private UserDTO convertToUserDTO(User user) {
        UserDTO userDTO = new UserDTO();
        if (user != null) {
            userDTO.setId(user.getId());
            userDTO.setUsername(user.getUsername());
            userDTO.setEmail(user.getEmail());
            userDTO.setActive(user.isActive());
            userDTO.setVerified(user.isVerified());
            userDTO.setRoles(user.getRoles() != null ? new HashSet<>(user.getRoles()) : new HashSet<>());
        }
        return userDTO;
    }
}