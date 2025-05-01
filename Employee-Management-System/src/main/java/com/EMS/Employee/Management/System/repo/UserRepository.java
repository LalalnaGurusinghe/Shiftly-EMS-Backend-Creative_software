package com.EMS.Employee.Management.System.repo;

import com.EMS.Employee.Management.System.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User , Long> {
    Optional<User> findByEmail(String email);
    boolean existsByEmployeeNumber(String employeeNumber);
    boolean existsByEmail(String email);
}
