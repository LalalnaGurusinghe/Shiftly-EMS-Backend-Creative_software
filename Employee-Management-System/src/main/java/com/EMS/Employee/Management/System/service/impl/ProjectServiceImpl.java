package com.EMS.Employee.Management.System.service.impl;

import com.EMS.Employee.Management.System.dto.ProjectDTO;
import com.EMS.Employee.Management.System.entity.ProjectEntity;
import com.EMS.Employee.Management.System.entity.TeamEntity;
import com.EMS.Employee.Management.System.entity.DepartmentEntity;
import com.EMS.Employee.Management.System.repo.ProjectRepo;
import com.EMS.Employee.Management.System.repo.TeamRepo;
import com.EMS.Employee.Management.System.repo.DepartmentRepo;
import com.EMS.Employee.Management.System.service.ProjectService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import com.EMS.Employee.Management.System.entity.User;
import com.EMS.Employee.Management.System.repo.UserRepo;

@Service
public class ProjectServiceImpl implements ProjectService {
    private final ProjectRepo projectRepo;
    private final TeamRepo teamRepo;
    private final DepartmentRepo departmentRepo;
    private final UserRepo userRepo;

    @Autowired
    public ProjectServiceImpl(ProjectRepo projectRepo, TeamRepo teamRepo, DepartmentRepo departmentRepo, UserRepo userRepo) {
        this.projectRepo = projectRepo;
        this.teamRepo = teamRepo;
        this.departmentRepo = departmentRepo;
        this.userRepo = userRepo;
    }

    // Admin: Create project
    @Override
    @Transactional
    public ProjectDTO createProject(ProjectDTO dto) {
        TeamEntity team = null;
        DepartmentEntity department = null;
        User createdBy = null;

        if (dto.getTeamName() != null) {
            team = teamRepo.findByName(dto.getTeamName())
                    .orElseThrow(() -> new RuntimeException("Team not found"));
        } else if (dto.getTeamId() != null) {
            team = teamRepo.findById(dto.getTeamId())
                    .orElseThrow(() -> new RuntimeException("Team not found"));
        }

        if (dto.getDepartmentName() != null) {
            department = departmentRepo.findByName(dto.getDepartmentName())
                    .orElseThrow(() -> new RuntimeException("Department not found"));
        } else if (dto.getDepartmentId() != null) {
            department = departmentRepo.findById(dto.getDepartmentId())
                    .orElseThrow(() -> new RuntimeException("Department not found"));
        }

        if (dto.getCreatedByUserId() != null) {
            createdBy = userRepo.findById(dto.getCreatedByUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        } else {
            throw new RuntimeException("Created by userId is required");
        }

        ProjectEntity entity = new ProjectEntity();
        BeanUtils.copyProperties(dto, entity);
        entity.setTeam(team);
        entity.setDepartment(department);
        entity.setCreatedBy(createdBy);
        entity.setProgress(dto.getProgress());
        ProjectEntity saved = projectRepo.save(entity);
        return toDTO(saved);
    }

    // Admin: Update project
    @Override
    @Transactional
    public ProjectDTO updateProject(Long projectId, ProjectDTO dto) {
        ProjectEntity entity = projectRepo.findById(projectId).orElseThrow(() -> new RuntimeException("Project not found"));
        if (dto.getName() != null) entity.setName(dto.getName());
        if (dto.getDescription() != null) entity.setDescription(dto.getDescription());
        if (dto.getStartDate() != null) entity.setStartDate(dto.getStartDate());
        if (dto.getEndDate() != null) entity.setEndDate(dto.getEndDate());
        if (dto.getTeamId() != null) {
            TeamEntity team = teamRepo.findById(dto.getTeamId()).orElseThrow(() -> new RuntimeException("Team not found"));
            entity.setTeam(team);
        }
        if (dto.getDepartmentId() != null) {
            DepartmentEntity department = departmentRepo.findById(dto.getDepartmentId()).orElseThrow(() -> new RuntimeException("Department not found"));
            entity.setDepartment(department);
        }
        entity.setProgress(dto.getProgress());
        ProjectEntity saved = projectRepo.save(entity);
        return toDTO(saved);
    }

    // Admin: Delete project
    @Override
    @Transactional
    public void deleteProject(Long projectId) {
        projectRepo.deleteById(projectId);
    }

    // Admin & Employee: View all projects
    @Override
    public List<ProjectDTO> getAllProjects() {
        return projectRepo.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    // Employee: View projects by team
    @Override
    public List<ProjectDTO> getProjectsByTeamId(Long teamId) {
        return projectRepo.findByTeam_TeamId(teamId).stream().map(this::toDTO).collect(Collectors.toList());
    }

    private ProjectDTO toDTO(ProjectEntity entity) {
        ProjectDTO dto = new ProjectDTO();
        BeanUtils.copyProperties(entity, dto);
        dto.setTeamId(entity.getTeam() != null ? entity.getTeam().getTeamId() : null);
        dto.setTeamName(entity.getTeam() != null ? entity.getTeam().getName() : null);
        dto.setDepartmentId(entity.getDepartment() != null ? entity.getDepartment().getDepartmentId() : null);
        dto.setDepartmentName(entity.getDepartment() != null ? entity.getDepartment().getName() : null);
        dto.setProgress(entity.getProgress());
        dto.setCreatedByUserId(entity.getCreatedBy() != null ? entity.getCreatedBy().getId() : null);
        dto.setCreatedByFirstName(entity.getCreatedBy() != null ? entity.getCreatedBy().getUsername() : null);
        return dto;
    }
} 