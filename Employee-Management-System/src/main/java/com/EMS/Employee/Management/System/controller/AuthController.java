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

@RestController // Marks this as a REST controller that returns JSON responses
@RequestMapping("/auth") // Base path for all endpoints in this controller
@CrossOrigin(origins = "*") // Allows requests from any origin (CORS)
public class AuthController {
    private final AuthenticationService authenticationService;
    private final UserRepo userRepo;

    // Constructor injection of dependencies
    public AuthController(AuthenticationService authenticationService, UserRepo userRepo) {
        this.authenticationService = authenticationService;
        this.userRepo = userRepo;
    }

    // Register a new employee
    @PostMapping("/register")
    public ResponseEntity<UserDTO> registerEmployee(@Valid @RequestBody RegisterRequestDTO registerRequestDTO) {
        // @Valid triggers validation on the DTO
        // @RequestBody binds the JSON payload to the DTO
        return ResponseEntity.ok(authenticationService.registerEmployee(registerRequestDTO));
    }

    // Login endpoint
    @PostMapping("/login")
    public ResponseEntity<UserDTO> login(@Valid @RequestBody LoginRequestDTO loginRequestDTO) {
        // Authenticate and get JWT token + user details
        LoginResponseDTO loginResponseDTO = authenticationService.login(loginRequestDTO);

        // Create secure HTTP-only cookie with JWT token
        ResponseCookie cookie = ResponseCookie.from("JWT", loginResponseDTO.getJwttoken())
                .httpOnly(true) // Prevent JavaScript access
                .secure(true) // Only send over HTTPS
                .path("/") // Available on all paths
                .maxAge(1 * 60 * 60) // 1 hour expiration
                .sameSite("Strict") // Prevent CSRF attacks
                .build();

        // Return response with cookie in header and user data in body
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(loginResponseDTO.getUserDTO());
    }

    // Logout endpoint
    @PostMapping("/logout")
    public ResponseEntity<String> logout() {
        return authenticationService.logout();
    }

    // Get current authenticated user
    @GetMapping("/currentuser")
    public ResponseEntity<?> getCurrentUser(Authentication authentication) {
        // Spring Security injects the authentication object
        if (authentication == null) {
            return ResponseEntity.status(401).body("Unauthorized");
        }
        // Get username from authentication object
        String username = authentication.getName();
        User user = userRepo.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return ResponseEntity.ok(convertToUserDTO(user));
    }

    // Helper method to convert User entity to DTO
    private UserDTO convertToUserDTO(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setUsername(user.getUsername());
        userDTO.setEmail(user.getEmail());
        return userDTO;
    }
}