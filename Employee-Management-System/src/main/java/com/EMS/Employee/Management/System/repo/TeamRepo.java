package com.EMS.Employee.Management.System.repo;

import com.EMS.Employee.Management.System.entity.TeamEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TeamRepo extends JpaRepository<TeamEntity, Long> {
    List<TeamEntity> findByDepartment_DepartmentId(Long departmentId);
} 