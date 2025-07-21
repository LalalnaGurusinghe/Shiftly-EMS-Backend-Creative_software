package com.EMS.Employee.Management.System.service.impl;

import com.EMS.Employee.Management.System.dto.ChangePasswordDTO;
import com.EMS.Employee.Management.System.dto.UserDTO;
import com.EMS.Employee.Management.System.entity.User;
import com.EMS.Employee.Management.System.repo.UserRepo;
import com.EMS.Employee.Management.System.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import com.EMS.Employee.Management.System.entity.EmployeeEntity;
import com.EMS.Employee.Management.System.repo.EmployeeRepo;
import com.EMS.Employee.Management.System.repo.DepartmentRepo;
import com.EMS.Employee.Management.System.entity.DepartmentEntity;

@Service
public class UserServiceImpl implements UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;
    private final JavaMailSender mailSender;
    private final EmployeeRepo employeeRepo;
    private final DepartmentRepo departmentRepo;

    public UserServiceImpl(UserRepo userRepo, PasswordEncoder passwordEncoder, JavaMailSender mailSender, EmployeeRepo employeeRepo, DepartmentRepo departmentRepo) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
        this.mailSender = mailSender;
        this.employeeRepo = employeeRepo;
        this.departmentRepo = departmentRepo;
    }

    @Override
    public UserDTO getUserById(Long id) {
        User user = userRepo.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        return convertToUserDTO(user);
    }

    @Override
    public User getUserEntityById(Long id) {
        return userRepo.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
    }

    @Override
    public UserDTO getUserByUsername(String username) {
        User user = userRepo.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));
        return convertToUserDTO(user);
    }

    @Override
    public List<UserDTO> getAllUsers() {
        return userRepo.findAll().stream()
                .map(this::convertToUserDTO)
                .collect(Collectors.toList());
    }

    @Override
    public UserDTO changePassword(Long id, ChangePasswordDTO changePasswordDTO) {
        User user = userRepo.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        if (!passwordEncoder.matches(changePasswordDTO.getCurrentPassword(), user.getPassword())) {
            throw new RuntimeException("Current password is incorrect");
        }
        if (!changePasswordDTO.getNewPassword().equals(changePasswordDTO.getConfirmPassword())) {
            throw new RuntimeException("New password and confirm password do not match");
        }
        user.setPassword(passwordEncoder.encode(changePasswordDTO.getNewPassword()));
        User updatedUser = userRepo.save(user);
        return convertToUserDTO(updatedUser);
    }

    @Override
    public void deleteUser(Long id) {
        User user = userRepo.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        userRepo.delete(user);
    }

    @Override
    public UserDTO updateUser(Long id, UserDTO userDTO) {
        User user = userRepo.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        if (userDTO.getUsername() != null && !userDTO.getUsername().equals(user.getUsername())) {
            if (userRepo.existsByUsername(userDTO.getUsername())) {
                throw new RuntimeException("Username already exists");
            }
            user.setUsername(userDTO.getUsername());
        }
        if (userDTO.getEmail() != null && !userDTO.getEmail().equals(user.getEmail())) {
            if (userRepo.existsByEmail(userDTO.getEmail())) {
                throw new RuntimeException("Email already exists");
            }
            user.setEmail(userDTO.getEmail());
        }
        User updatedUser = userRepo.save(user);
        return convertToUserDTO(updatedUser);
    }

    @Override
    @Transactional
    public UserDTO verifyAndUpdateUserRole(Long id, String role) {
        if (!isValidRole(role)) {
            throw new IllegalArgumentException("Invalid role specified: " + role);
        }
        User user = userRepo.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        Set<String> roles = new HashSet<>();
        switch (role.toUpperCase()) {
            case "SUPER_ADMIN":
                roles.add("SUPER_ADMIN");
                roles.add("ADMIN");
                roles.add("USER");
                break;
            case "ADMIN":
                roles.add("ADMIN");
                roles.add("USER");
                break;
            case "USER":
                roles.add("USER");
                break;
        }
        user.setRoles(roles);
        user.setVerified(true);
        User updatedUser = userRepo.save(user);
        return convertToUserDTO(updatedUser);
    }

    @Override
    @Transactional
    public UserDTO verifyAndUpdateUserRoleAndProfile(Long id, String role, String designation, Long departmentId) {
        if (!isValidRole(role)) {
            throw new IllegalArgumentException("Invalid role specified: " + role);
        }
        User user = userRepo.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        Set<String> roles = new HashSet<>();
        switch (role.toUpperCase()) {
            case "SUPER_ADMIN":
                roles.add("SUPER_ADMIN");
                roles.add("ADMIN");
                roles.add("USER");
                break;
            case "ADMIN":
                roles.add("ADMIN");
                roles.add("USER");
                break;
            case "USER":
                roles.add("USER");
                break;
        }
        user.setRoles(roles);
        user.setVerified(true);
        userRepo.save(user);
        // Update employee profile
        EmployeeEntity employee = employeeRepo.findByUser_Id(user.getId());
        if (employee != null) {
            if (designation != null) employee.setDesignation(designation);
            if (departmentId != null) {
                DepartmentEntity department = departmentRepo.findById(departmentId).orElseThrow(() -> new RuntimeException("Department not found"));
                employee.setDepartment(department);
            }
            employeeRepo.save(employee);
        }
        return convertToUserDTO(user);
    }

    @Override
    @Transactional
    public List<UserDTO> verifyAllUnverifiedEmployees(String role) {
        if (!isValidRole(role)) {
            throw new IllegalArgumentException("Invalid role specified: " + role);
        }
        List<User> unverifiedUsers = userRepo.findByIsVerifiedFalse();
        Set<String> roles = new HashSet<>();
        switch (role.toUpperCase()) {
            case "SUPER_ADMIN":
                roles.add("SUPER_ADMIN");
                roles.add("ADMIN");
                roles.add("USER");
                break;
            case "ADMIN":
                roles.add("ADMIN");
                roles.add("USER");
                break;
            case "USER":
                roles.add("USER");
                break;
        }
        unverifiedUsers.forEach(user -> {
            user.setRoles(roles);
            user.setVerified(true);
            userRepo.save(user);
            logger.info("Verified user: {} with role: {}", user.getUsername(), role);
        });
        return unverifiedUsers.stream()
                .map(this::convertToUserDTO)
                .collect(Collectors.toList());
    }

    private boolean isValidRole(String role) {
        if (role == null) {
            return false;
        }
        return switch (role.toUpperCase()) {
            case "USER", "ADMIN", "SUPER_ADMIN" -> true;
            default -> false;
        };
    }

    private UserDTO convertToUserDTO(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setUsername(user.getUsername());
        userDTO.setEmail(user.getEmail());
        userDTO.setActive(user.isActive());
        userDTO.setVerified(user.isVerified());
        userDTO.setRoles(user.getRoles());
        return userDTO;
    }


}