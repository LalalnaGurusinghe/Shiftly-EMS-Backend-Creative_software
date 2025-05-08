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
    private static final Logger logger = LoggerFactory.getLogger(AuthenticationServiceImpl.class);
    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final JavaMailSender mailSender;

    public AuthenticationServiceImpl(UserRepo userRepo, PasswordEncoder passwordEncoder,
                                     AuthenticationManager authenticationManager, JwtService jwtService,
                                     JavaMailSender mailSender) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.mailSender = mailSender;
    }

    @Override
    public UserDTO registerEmployee(RegisterRequestDTO registerRequestDTO) {
        if (userRepo.existsByUsername(registerRequestDTO.getUsername())) {
            throw new RuntimeException("Username already exists");
        }
        if (userRepo.existsByEmail(registerRequestDTO.getEmail())) {
            throw new RuntimeException("Email already exists");
        }
        User user = new User();
        user.setUsername(registerRequestDTO.getUsername());
        user.setEmail(registerRequestDTO.getEmail());
        user.setPassword(passwordEncoder.encode(registerRequestDTO.getPassword()));
        user.setRoles(new HashSet<>());
        User savedUser = userRepo.save(user);
        logger.info("Registered user: username={}, email={}", savedUser.getUsername(), savedUser.getEmail());
        sendRegistrationEmail(savedUser);
        return convertToUserDTO(savedUser);
    }

    @Override
    public LoginResponseDTO login(LoginRequestDTO loginRequestDTO) {
        User user = userRepo.findByUsername(loginRequestDTO.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));
        if (!user.isVerified()) {
            throw new RuntimeException("Account not verified");
        }
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequestDTO.getUsername(),
                        loginRequestDTO.getPassword()
                )
        );
        String jwtToken = jwtService.generateToken(user);
        return LoginResponseDTO.builder()
                .jwttoken(jwtToken)
                .userDTO(convertToUserDTO(user))
                .build();
    }

    @Override
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
                .body("Logout successful");
    }

    @Override
    public void sendVerificationEmail(User user, String role) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("lalanagurusinghe@gmail.com");
        message.setTo(user.getEmail());
        message.setSubject("Account Verification - Employee Management System");
        String displayRole = role.replace("ROLE_", "").replace("_", " ");
        String emailContent = "Dear " + user.getUsername() + ",\n\n" +
                "Your account has been verified by the Super Admin.\n" +
                "Assigned Role: " + displayRole + "\n" +
                "Username: " + user.getUsername() + "\n" +
                "Please log in using your credentials at: http://localhost:3000/login\n\n" +
                "Best regards,\nEmployee Management Team";
        message.setText(emailContent);
        try {
            logger.info("Preparing verification email: to={}, username={}, role={}, content={}",
                    user.getEmail(), user.getUsername(), displayRole, emailContent);
            mailSender.send(message);
            logger.info("Verification email sent to {}", user.getEmail());
        } catch (Exception e) {
            logger.error("Failed to send verification email to {}: {}", user.getEmail(), e.getMessage());
        }
    }

    private synchronized void sendRegistrationEmail(User user) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("lalanagurusinghe@gmail.com");
        message.setTo(user.getEmail());
        message.setSubject("Registration Confirmation - Employee Management System");
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
            mailSender.send(message);
            logger.info("Registration email sent to {}", user.getEmail());
        } catch (Exception e) {
            logger.error("Failed to send registration email to {}: {}", user.getEmail(), e.getMessage());
        }
    }

    private UserDTO convertToUserDTO(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setUsername(user.getUsername());
        userDTO.setEmail(user.getEmail());
        return userDTO;
    }
}