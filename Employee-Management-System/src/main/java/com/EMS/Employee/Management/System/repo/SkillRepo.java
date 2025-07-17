package com.EMS.Employee.Management.System.repo;

import com.EMS.Employee.Management.System.entity.SkillEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SkillRepo extends JpaRepository<SkillEntity, Long> {
    List<SkillEntity> findByEmployee_EmployeeId(Integer employeeId);
} 