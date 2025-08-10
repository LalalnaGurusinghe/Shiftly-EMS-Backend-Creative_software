package com.EMS.Employee.Management.System.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.EMS.Employee.Management.System.entity.EmployeeEntity;

@Repository
public interface EmployeeRepo extends JpaRepository<EmployeeEntity, Integer> {
    Optional<EmployeeEntity> findByUser_Id(Long userId);

    boolean existsByUser_Id(Long userId);

    @Modifying
    @Transactional
    void deleteByUser_Id(Long userId);
}