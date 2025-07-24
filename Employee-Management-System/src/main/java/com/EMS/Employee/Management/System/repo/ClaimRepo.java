package com.EMS.Employee.Management.System.repo;

import com.EMS.Employee.Management.System.entity.ClaimEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClaimRepo extends JpaRepository<ClaimEntity, Long> {
    List<ClaimEntity> findByUser_Id(Long userId);
    List<ClaimEntity> findByUser_Department(String department);
}