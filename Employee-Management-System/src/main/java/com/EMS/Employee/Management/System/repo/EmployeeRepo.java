package com.EMS.Employee.Management.System.repo;

import com.EMS.Employee.Management.System.entity.EmployeeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRepo extends JpaRepository<EmployeeEntity, Integer> {
    EmployeeEntity findByUser_Id(Long userId);
}