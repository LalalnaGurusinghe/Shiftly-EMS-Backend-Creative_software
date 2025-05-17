package com.EMS.Employee.Management.System.service.impl;

import com.EMS.Employee.Management.System.dto.LoginRequestDTO;
import com.EMS.Employee.Management.System.dto.LoginResponseDTO;
import com.EMS.Employee.Management.System.dto.RegisterRequestDTO;
import com.EMS.Employee.Management.System.dto.UserDTO;
import com.EMS.Employee.Management.System.entity.User;
import com.EMS.Employee.Management.System.repo.UserRepo;
import com.EMS.Employee.Management.System.service.AuthenticationService;
import com.EMS.Employee.Management.System.service.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    // Logger for logging important events or errors
    //This is not Mandatory but a good practice to have logging in place guys
    private static final Logger logger = LoggerFactory.getLogger(AuthenticationServiceImpl.class);

    // Required dependencies
    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final JavaMailSender mailSender;

    // Constructor-based dependency injection
    public AuthenticationServiceImpl(UserRepo userRepo, PasswordEncoder passwordEncoder,
                                     AuthenticationManager authenticationManager, JwtService jwtService,
                                     JavaMailSender mailSender) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.mailSender = mailSender;
    }

    // Method to register a new employee
    @Override
    public UserDTO registerEmployee(RegisterRequestDTO registerRequestDTO) {
        // Check for existing username
        if (userRepo.existsByUsername(registerRequestDTO.getUsername())) {
            throw new RuntimeException("Username already exists");
        }

        // Check for existing email
        if (userRepo.existsByEmail(registerRequestDTO.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        // Create a new User entity and set its fields
        User user = new User();
        user.setUsername(registerRequestDTO.getUsername());
        user.setEmail(registerRequestDTO.getEmail());
        user.setPassword(passwordEncoder.encode(registerRequestDTO.getPassword())); // Encrypt password
        user.setRoles(new HashSet<>()); // Initialize empty roles (will be assigned by admin later)

        // Save the user in the database
        User savedUser = userRepo.save(user);

        // Log registration info
        logger.info("Registered user: username={}, email={}", savedUser.getUsername(), savedUser.getEmail());

        // Send registration confirmation email
        sendRegistrationEmail(savedUser);

        // Convert entity to DTO and return
        return convertToUserDTO(savedUser);
    }

    // Method to authenticate user login and return a JWT token
    @Override
    public LoginResponseDTO login(LoginRequestDTO loginRequestDTO) {
        // Fetch user by username
        User user = userRepo.findByUsername(loginRequestDTO.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Check if account is verified (approved by super admin)
        if (!user.isVerified()) {
            throw new RuntimeException("Account not verified");
        }

        // Authenticate user using Spring Security
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequestDTO.getUsername(),
                        loginRequestDTO.getPassword()
                )
        );

        // Generate JWT token after successful authentication
        String jwtToken = jwtService.generateToken(user);

        // Return token and user data to client
        return LoginResponseDTO.builder()
                .jwttoken(jwtToken)
                .userDTO(convertToUserDTO(user))
                .build();
    }

    // Method to clear JWT cookie and return logout success message
    @Override
    public ResponseEntity<String> logout() {
        // Create an expired cookie to delete JWT from client
        ResponseCookie cookie = ResponseCookie.from("JWT", "")
                .httpOnly(true)   // Prevent JS access (security)
                .secure(true)     // HTTPS only
                .path("/")        // Applies to whole app
                .maxAge(0)        // Expire immediately
                .sameSite("Strict")
                .build();

        // Send response with expired cookie
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body("Logout successful");
    }

    // Method to send account verification email after super admin approval
    @Override
    public void sendVerificationEmail(User user, String role) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("lalanagurusinghe@gmail.com"); // Your sending email
        message.setTo(user.getEmail());
        message.setSubject("Account Verification - Employee Management System");

        // Format the role for display this is for better readability
        String displayRole = role.replace("ROLE_", "").replace("_", " ");

        // Build email content
        String emailContent = "Dear " + user.getUsername() + ",\n\n" +
                "Your account has been verified by the Super Admin.\n" +
                "Assigned Role: " + displayRole + "\n" +
                "Username: " + user.getUsername() + "\n" +
                "Please log in using your credentials at: http://localhost:3000/login\n\n" +
                "Best regards,\nEmployee Management Team";

        message.setText(emailContent);

        try {
            // Log sending attempt
            logger.info("Preparing verification email: to={}, username={}, role={}, content={}",
                    user.getEmail(), user.getUsername(), displayRole, emailContent);

            mailSender.send(message); // Send the email

            logger.info("Verification email sent to {}", user.getEmail());
        } catch (Exception e) {
            logger.error("Failed to send verification email to {}: {}", user.getEmail(), e.getMessage());
        }
    }

    // Private method to send registration confirmation email
    private synchronized void sendRegistrationEmail(User user) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("lalanagurusinghe@gmail.com");
        message.setTo(user.getEmail());
        message.setSubject("Registration Confirmation - Employee Management System");

        // Email body text
        String emailContent = "Dear " + user.getUsername() + ",\n\n" +
                "Your registration has been successfully received.\n" +
                "Please wait for Super Admin verification.\n" +
                "You will receive another email once your account is verified with your assigned role.\n\n" +
                "Best regards,\nEmployee Management Team";

        message.setText(emailContent);

        try {
            logger.info("Preparing registration email: to={}, username={}, content={}",
                    user.getEmail(), user.getUsername(), emailContent);

            logger.info("Message object before send: to={}, subject={}, text={}",
                    message.getTo()[0], message.getSubject(), message.getText());

            mailSender.send(message); // Actually send the email

            logger.info("Registration email sent to {}", user.getEmail());
        } catch (Exception e) {
            logger.error("Failed to send registration email to {}: {}", user.getEmail(), e.getMessage());
        }
    }

    // Utility method to convert User entity to UserDTO (safe to return to frontend)
    private UserDTO convertToUserDTO(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setUsername(user.getUsername());
        userDTO.setEmail(user.getEmail());
        return userDTO;
    }
}

//These Logger part are not needed but i enter those for better monitoring, debugging, and maintainability
