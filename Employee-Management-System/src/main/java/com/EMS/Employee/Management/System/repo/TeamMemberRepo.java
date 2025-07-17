package com.EMS.Employee.Management.System.repo;

import com.EMS.Employee.Management.System.entity.TeamMemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TeamMemberRepo extends JpaRepository<TeamMemberEntity, Long> {
    List<TeamMemberEntity> findByTeam_TeamId(Long teamId);
    List<TeamMemberEntity> findByEmployee_EmployeeId(Integer employeeId);
} 