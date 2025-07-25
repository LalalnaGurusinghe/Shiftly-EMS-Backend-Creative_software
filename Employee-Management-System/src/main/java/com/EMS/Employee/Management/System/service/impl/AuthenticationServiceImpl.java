package com.EMS.Employee.Management.System.service.impl;

import com.EMS.Employee.Management.System.dto.LoginRequestDTO;
import com.EMS.Employee.Management.System.dto.LoginResponseDTO;
import com.EMS.Employee.Management.System.dto.RegisterRequestDTO;
import com.EMS.Employee.Management.System.dto.UserDTO;
import com.EMS.Employee.Management.System.entity.User;
import com.EMS.Employee.Management.System.exception.AccountNotVerifiedException;
import com.EMS.Employee.Management.System.exception.UserNotFoundException;
import com.EMS.Employee.Management.System.repo.UserRepo;
import com.EMS.Employee.Management.System.service.AuthenticationService;
import com.EMS.Employee.Management.System.service.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.http.ResponseEntity;

import com.EMS.Employee.Management.System.repo.DepartmentRepo;
import com.EMS.Employee.Management.System.entity.DepartmentEntity;

import java.util.HashSet;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationServiceImpl.class);

    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final JavaMailSender mailSender;
    private final DepartmentRepo departmentRepo;

    public AuthenticationServiceImpl(UserRepo userRepo, PasswordEncoder passwordEncoder,
                                     AuthenticationManager authenticationManager, JwtService jwtService,
                                     JavaMailSender mailSender, DepartmentRepo departmentRepo) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.mailSender = mailSender;
        this.departmentRepo = departmentRepo;
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
        user.setActive(true);
        user.setVerified(false); // Requires super admin verification
        user.setRoles(new HashSet<>());

        User savedUser = userRepo.save(user);
        logger.info("Registered user: username={}, email={}", savedUser.getUsername(), savedUser.getEmail());
        sendRegistrationEmail(savedUser);

        return convertToUserDTO(savedUser);
    }

    @Override
    public LoginResponseDTO login(LoginRequestDTO loginRequestDTO) {
        User user = userRepo.findByUsername(loginRequestDTO.getUsername())
                .orElseThrow(() -> new UserNotFoundException("Invalid username or password"));

        if (!user.isVerified()) {
            throw new AccountNotVerifiedException("Account not verified");
        }

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequestDTO.getUsername(),
                        loginRequestDTO.getPassword()
                )
        );

        String jwtToken = jwtService.generateToken(user);

        // Return response with JWT token for localStorage storage
        return LoginResponseDTO.builder()
                .jwttoken(jwtToken)
                .userDTO(convertToUserDTO(user))
                .build();
    }

    @Override
    public ResponseEntity<String> logout() {
        // For localStorage-based JWT, logout is handled on the frontend
        // by removing the token from localStorage
        return ResponseEntity.ok("Logged out successfully");
    }

    @Override
    public void sendVerificationEmail(User user, String role, String designation, String department) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("lalanagurusinghe@gmail.com");
        message.setTo(user.getEmail());
        message.setSubject("Account Verification - Employee Management System");

        String displayRole = role.replace("ROLE_", "").replace("_", " ");
        String departmentName = "";
        if (department != null && !department.isEmpty()) {
            DepartmentEntity departmentEntity = departmentRepo.findByName(department).orElse(null);
            if (departmentEntity != null) departmentName = departmentEntity.getName();
        }
        String emailContent = "Dear " + user.getUsername() + ",\n\n" +
                "Your account has been verified by the Super Admin.\n" +
                "Assigned Role: " + displayRole + "\n" +
                "Designation: " + (designation != null ? designation : "-") + "\n" +
                "Department: " + (!departmentName.isEmpty() ? departmentName : "-") + "\n" +
                "Reporting Person: " + (user.getReportingPerson() != null ? user.getReportingPerson() : "-") + "\n" +
                "Reporting Person Email: " + (user.getReportingPersonEmail() != null ? user.getReportingPersonEmail() : "-") + "\n" +
                "Username: " + user.getUsername() + "\n" +
                "Please log in using your credentials at: http://localhost:3000/login\n\n" +
                "Best regards,\nEmployee Management Team";

        message.setText(emailContent);

        try {
            logger.info("Preparing verification email: to={}, username={}, role={}, designation={}, department={}, content={}",
                    user.getEmail(), user.getUsername(), displayRole, designation, departmentName, emailContent);
            mailSender.send(message);
            logger.info("Verification email sent to {}", user.getEmail());
        } catch (Exception e) {
            logger.error("Failed to send verification email to {}: {}", user.getEmail(), e.getMessage());
        }
    }

    private void sendRegistrationEmail(User user) {
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
            mailSender.send(message);
            logger.info("Registration email sent to {}", user.getEmail());
        } catch (Exception e) {
            logger.error("Failed to send registration email to {}: {}", user.getEmail(), e.getMessage());
        }
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
            userDTO.setDesignation(user.getDesignation());
            userDTO.setDepartment(user.getDepartment());
            userDTO.setCreatedAt(user.getCreatedAt());
        }
        return userDTO;
    }
}