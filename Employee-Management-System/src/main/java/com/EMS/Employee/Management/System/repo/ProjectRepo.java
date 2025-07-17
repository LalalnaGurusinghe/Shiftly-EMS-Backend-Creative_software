package com.EMS.Employee.Management.System.repo;

import com.EMS.Employee.Management.System.entity.ProjectEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ProjectRepo extends JpaRepository<ProjectEntity, Long> {
    List<ProjectEntity> findByTeam_TeamId(Long teamId);
} 