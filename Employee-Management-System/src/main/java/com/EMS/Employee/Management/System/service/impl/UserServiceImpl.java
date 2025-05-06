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

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;
    private final JavaMailSender mailSender;

    public UserServiceImpl(UserRepo userRepo, PasswordEncoder passwordEncoder, JavaMailSender mailSender) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
        this.mailSender = mailSender;
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
    public UserDTO updateUserRole(Long id, String role) {
        User user = userRepo.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        Set<String> roles = new HashSet<>();
        switch (role.toUpperCase()) {
            case "SUPER_ADMIN":
                roles.add("ROLE_SUPER_ADMIN");
                roles.add("ROLE_ADMIN");
                roles.add("ROLE_USER");
                break;
            case "ADMIN":
                roles.add("ROLE_ADMIN");
                roles.add("ROLE_USER");
                break;
            case "USER":
                roles.add("ROLE_USER");
                break;
            default:
                throw new RuntimeException("Invalid role specified");
        }
        user.setRoles(roles);
        user.setVerified(true);
        User updatedUser = userRepo.save(user);
        return convertToUserDTO(updatedUser);
    }

    @Override
    public List<UserDTO> verifyAllUnverifiedEmployees(String role) {
        List<User> unverifiedUsers = userRepo.findAll().stream()
                .filter(user -> !user.isVerified())
                .collect(Collectors.toList());
        unverifiedUsers.forEach(user -> {
            Set<String> roles = new HashSet<>();
            switch (role.toUpperCase()) {
                case "SUPER_ADMIN":
                    roles.add("ROLE_SUPER_ADMIN");
                    roles.add("ROLE_ADMIN");
                    roles.add("ROLE_USER");
                    break;
                case "ADMIN":
                    roles.add("ROLE_ADMIN");
                    roles.add("ROLE_USER");
                    break;
                case "USER":
                    roles.add("ROLE_USER");
                    break;
                default:
                    throw new RuntimeException("Invalid role specified");
            }
            user.setRoles(roles);
            user.setVerified(true);
            userRepo.save(user);
            logger.info("Verified user: {}", user.getUsername());
        });
        return unverifiedUsers.stream()
                .map(this::convertToUserDTO)
                .collect(Collectors.toList());
    }

    private UserDTO convertToUserDTO(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setUsername(user.getUsername());
        userDTO.setEmail(user.getEmail());
        return userDTO;
    }
}