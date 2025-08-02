package com.EMS.Employee.Management.System.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.EMS.Employee.Management.System.entity.DepartmentEntity;

@Repository
public interface DepartmentRepo extends JpaRepository<DepartmentEntity, Long> {
    Optional<DepartmentEntity> findByName(String departmentName);
    Optional<DepartmentEntity> findById(Long id);
} 