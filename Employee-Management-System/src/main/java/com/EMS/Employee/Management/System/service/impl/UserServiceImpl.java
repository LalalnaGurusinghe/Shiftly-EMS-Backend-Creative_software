package com.EMS.Employee.Management.System.service.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.EMS.Employee.Management.System.dto.ChangePasswordDTO;
import com.EMS.Employee.Management.System.dto.DetailUserDTO;
import com.EMS.Employee.Management.System.dto.EmployeeDTO;
import com.EMS.Employee.Management.System.dto.UserDTO;
import com.EMS.Employee.Management.System.entity.DepartmentEntity;
import com.EMS.Employee.Management.System.entity.User;
import com.EMS.Employee.Management.System.repo.DepartmentRepo;
import com.EMS.Employee.Management.System.repo.EmployeeRepo;
import com.EMS.Employee.Management.System.repo.UserRepo;
import com.EMS.Employee.Management.System.service.EmployeeService;
import com.EMS.Employee.Management.System.service.UserService;

@Service
public class UserServiceImpl implements UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;
    private final JavaMailSender mailSender;
    private final EmployeeRepo employeeRepo;
    private final DepartmentRepo departmentRepo;
    private EmployeeService employeeService;

    public UserServiceImpl(UserRepo userRepo, PasswordEncoder passwordEncoder, JavaMailSender mailSender, EmployeeRepo employeeRepo, DepartmentRepo departmentRepo, EmployeeService employeeService) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
        this.mailSender = mailSender;
        this.employeeRepo = employeeRepo;
        this.departmentRepo = departmentRepo;
        this.employeeService = employeeService;
    }

    @Override
    public List<UserDTO> getAllUnverifiedUsers() {
        return userRepo.findByIsVerifiedFalse().stream()
                .map(this::convertToUserDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<UserDTO> getAllVerifiedUsers() {
        return userRepo.findByIsVerifiedTrue().stream()
                .map(this::convertToUserDTO)
                .collect(Collectors.toList());
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
    public List<UserDTO> getAll() {
        return userRepo.findAll().stream()
                .map(this::convertToUserDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<DetailUserDTO> getAllUsers() {
        return userRepo.findAll().stream()
            // include users that have USER role
            .filter(user -> user.getRoles() != null &&
                    user.getRoles().stream().anyMatch(r -> "USER".equalsIgnoreCase(r)))
            // exclude any that also have ADMIN or SUPER_ADMIN
            .filter(user -> user.getRoles().stream()
                    .noneMatch(r -> "ADMIN".equalsIgnoreCase(r) || "SUPER_ADMIN".equalsIgnoreCase(r)))
            .map(user -> {
                DetailUserDTO dto = new DetailUserDTO();
                dto.setId(user.getId());
                dto.setUsername(user.getUsername());
                dto.setEmail(user.getEmail());
                dto.setRoles(user.getRoles().stream().map(Object::toString).collect(Collectors.toList()));

                // enrich with employee details (if any)
                EmployeeDTO employee = employeeService.getEmployeeByUserId(user.getId());
                if (employee != null) {
                    dto.setDesignationId(employee.getDesignationId());
                    dto.setDesignationName(employee.getDesignationName());
                    dto.setDepartment(employee.getDepartment());
                    dto.setDepartmentId(employee.getDepartmentId());
                    dto.setReportingPerson(employee.getReportingPerson());
                    dto.setReportingPersonId(employee.getReportingPersonId());
                    dto.setReportingPersonEmail(employee.getReportingPersonEmail());
                }
                return dto;
            })
            .collect(Collectors.toList());
    }

    @Override
    public List<DetailUserDTO> getAllAdmins() {
        List<User> adminUsers = userRepo.findByRolesContaining("ADMIN").stream()
        .filter(user -> user.getRoles().stream()
            .noneMatch(role -> role.equalsIgnoreCase("SUPER_ADMIN")))
        .collect(Collectors.toList());

        return adminUsers.stream().map(user -> {
            DetailUserDTO dto = new DetailUserDTO();
            dto.setId(user.getId());
            dto.setUsername(user.getUsername());
            dto.setEmail(user.getEmail());
            dto.setRoles(user.getRoles().stream().map(Object::toString).collect(Collectors.toList()));

            EmployeeDTO employee = employeeService.getEmployeeByUserId(user.getId());
            if (employee != null) {
                dto.setDesignationId(employee.getDesignationId());
                dto.setDesignationName(employee.getDesignationName());
                dto.setDepartment(employee.getDepartment());
                dto.setDepartmentId(employee.getDepartmentId());
                dto.setReportingPerson(employee.getReportingPerson());
                dto.setReportingPersonId(employee.getReportingPersonId());
                dto.setReportingPersonEmail(employee.getReportingPersonEmail());
            }
            return dto;
        }).collect(Collectors.toList());
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
    @Transactional
    public void deleteUser(Long id) {
        List<DepartmentEntity> depts = departmentRepo.findByAdmin_Id(id);
        if (!depts.isEmpty()) {
            depts.forEach(d -> d.setAdmin(null));
            departmentRepo.saveAll(depts);
        }

        if (employeeRepo.existsByUser_Id(id)) {
            employeeRepo.deleteByUser_Id(id);
        }
        userRepo.deleteById(id);
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
    public UserDTO updateUserProfile(Long id, String designation, String department, String reportingPerson, String reportingPersonEmail) {
        logger.info("Updating user profile for user ID: {}", id);
        logger.info("Designation: {}, Department: {}, ReportingPerson: {}, ReportingPersonEmail: {}", 
                   designation, department, reportingPerson, reportingPersonEmail);
        
        User user = userRepo.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        logger.info("Found user: {}", user.getUsername());
        
        try {
            User updatedUser = userRepo.save(user);
            logger.info("Successfully saved user profile updates");
            return convertToUserDTO(updatedUser);
        } catch (Exception e) {
            logger.error("Error saving user profile updates: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to update user profile: " + e.getMessage());
        }
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

    @Override
    @Transactional
    public UserDTO verifyAndUpdateUserRoleAndProfile(Long id, String role) {
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
        return convertToUserDTO(user);
    }

    @Override
    public List<UserDTO> getAllAdminsWithoutDepartment() {
        // Get all departments with an admin assigned
        List<Long> adminIdsWithDepartment = departmentRepo.findAll().stream()
                .filter(dept -> dept.getAdmin() != null)
                .map(dept -> dept.getAdmin().getId())
                .collect(Collectors.toList());

        // Get all users with ADMIN role, and filter out those who are admins of any department
        return userRepo.findByRolesContaining("ADMIN").stream()
                .filter(user -> user.getRoles().stream()
                    .noneMatch(role -> role.equalsIgnoreCase("SUPER_ADMIN")))
                .filter(user -> !adminIdsWithDepartment.contains(user.getId()))
                .map(this::convertToUserDTO)
                .collect(Collectors.toList());
    }

    // @Override
    // public List<Object[]> getUsernameAndDesignationByDepartment(String department) {
    //     List<User> users = userRepo.findByDepartment(department);
    //     return users.stream()
    //             .map(u -> new Object[]{u.getUsername(), u.getDesignation()})
    //             .collect(Collectors.toList());
    // }

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

    // @Override
    // public AdminUserResponseDTO getAdminUserByDepartment(String department) {
    //     // TODO Auto-generated method stub
    //     throw new UnsupportedOperationException("Unimplemented method 'getAdminUserByDepartment'");
    // }

    // @Override
    // public AdminUserResponseDTO getAdminUserByDepartment(String department) {
    //     List<User> adminUsers = userRepo.findByDepartmentAndRolesContaining(department, "ADMIN");
        
    //     // Return the first admin user found, or null if no admin users exist
    //     for (User user : adminUsers) {
    //         EmployeeEntity employee = employeeRepo.findByUser_Id(user.getId());
    //         if (employee != null) {
    //             return new AdminUserResponseDTO(
    //                 employee.getFirstName(),
    //                 employee.getLastName(),
    //                 user.getEmail()
    //             );
    //         }
    //     }
    //     return null; // No admin user found for this department
    // }


}