package com.EMS.Employee.Management.System.repo;

import com.EMS.Employee.Management.System.entity.EducationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EducationRepo extends JpaRepository<EducationEntity, Long> {
    List<EducationEntity> findByEmployee_EmployeeId(Integer employeeId);
} 