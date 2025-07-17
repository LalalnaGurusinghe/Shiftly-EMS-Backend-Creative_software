package com.EMS.Employee.Management.System.controller;

import com.EMS.Employee.Management.System.dto.*;
import com.EMS.Employee.Management.System.entity.User;
import com.EMS.Employee.Management.System.repo.UserRepo;
import com.EMS.Employee.Management.System.service.AuthenticationService;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
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
    public ResponseEntity<UserDTO> login(@Valid @RequestBody LoginRequestDTO loginRequestDTO) {
        LoginResponseDTO loginResponseDTO = authenticationService.login(loginRequestDTO);
        ResponseCookie cookie = ResponseCookie.from("JWT", loginResponseDTO.getJwttoken())
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(1 * 60 * 60)
                .sameSite("Strict")
                .build();
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(loginResponseDTO.getUserDTO());
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout() {
        ResponseCookie cookie = ResponseCookie.from("JWT", "")
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(0)
                .sameSite("Strict")
                .build();
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body("Logged out successfully");
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
        userDTO.setId(user.getId());
        userDTO.setUsername(user.getUsername());
        userDTO.setEmail(user.getEmail());
        userDTO.setRoles(user.getRoles());
        return userDTO;
    }
}