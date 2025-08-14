package com.EMS.Employee.Management.System.repo;

import com.EMS.Employee.Management.System.entity.ClaimEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClaimRepo extends JpaRepository<ClaimEntity, Long> {
    List<ClaimEntity> findByEmployee_EmployeeId(int employeeId);
    List<ClaimEntity> findByEmployee_Department_Id(Long departmentId);
}