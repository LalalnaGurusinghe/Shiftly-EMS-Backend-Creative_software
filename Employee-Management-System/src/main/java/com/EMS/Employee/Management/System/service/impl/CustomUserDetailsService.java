package com.EMS.Employee.Management.System.service.impl;

import com.EMS.Employee.Management.System.entity.Role;
import com.EMS.Employee.Management.System.entity.User;
import com.EMS.Employee.Management.System.repo.RoleRepository;
import com.EMS.Employee.Management.System.repo.UserRepository;
import jakarta.validation.ValidationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public CustomUserDetailsService(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                new ArrayList<>() {{
                    add(new org.springframework.security.core.authority.SimpleGrantedAuthority("ROLE_" + user.getRole().getName()));
                }}
        );
    }

    public User registerUser(User user) {
        if (existsByEmail(user.getEmail())) {
            throw new ValidationException("Email already exists");
        }
        if (existsByEmployeeNumber(user.getEmployeeNumber())) {
            throw new ValidationException("Employee number already exists");
        }
        if (user.getPassword() == null || user.getPassword().length() < 8) {
            throw new ValidationException("Password must be at least 8 characters long");
        }

        String roleName = user.getEmployeeNumber().startsWith("ADM") ? "ADMIN" : "USER";
        Role role = roleRepository.findByName(roleName)
                .orElseGet(() -> {
                    Role newRole = new Role();
                    newRole.setName(roleName);
                    return roleRepository.save(newRole);
                });

        user.setRole(role);
        user.setPassword(passwordEncoder().encode(user.getPassword()));
        return userRepository.save(user);
    }

    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    public boolean existsByEmployeeNumber(String employeeNumber) {
        return userRepository.existsByEmployeeNumber(employeeNumber);
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    private PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}