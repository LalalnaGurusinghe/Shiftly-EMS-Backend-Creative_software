package com.EMS.Employee.Management.System.repo;

import com.EMS.Employee.Management.System.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepo extends JpaRepository<User, Long> {
    List<User> findByIsVerifiedFalse();
    Optional<User> findByUsername(String username);
    Optional<User> findById(Long userId);
    Optional<User> findByEmail(String email);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    List<User> findByRolesContaining(String role);
}