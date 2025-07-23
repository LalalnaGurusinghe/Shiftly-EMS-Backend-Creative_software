package com.EMS.Employee.Management.System.service.impl;

import com.EMS.Employee.Management.System.dto.ProjectDTO;
import com.EMS.Employee.Management.System.entity.ProjectEntity;
import com.EMS.Employee.Management.System.entity.TeamEntity;
import com.EMS.Employee.Management.System.entity.DepartmentEntity;
import com.EMS.Employee.Management.System.repo.ProjectRepo;
import com.EMS.Employee.Management.System.repo.TeamRepo;
import com.EMS.Employee.Management.System.repo.DepartmentRepo;
import com.EMS.Employee.Management.System.service.ProjectService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
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
        User user = null;

        // Handle team - check ID first, then name
        if (dto.getTeamId() != null) {
            team = teamRepo.findById(dto.getTeamId())
                    .orElseThrow(() -> new RuntimeException("Team not found with id: " + dto.getTeamId()));
        } else if (dto.getTeamName() != null) {
            team = teamRepo.findByName(dto.getTeamName())
                    .orElseThrow(() -> new RuntimeException("Team not found with name: " + dto.getTeamName()));
        } else {
            throw new RuntimeException("Team is required. Provide either teamId or teamName");
        }

        // Handle department - check ID first, then name
        if (dto.getDepartmentId() != null) {
            department = departmentRepo.findById(dto.getDepartmentId())
                    .orElseThrow(() -> new RuntimeException("Department not found with id: " + dto.getDepartmentId()));
        } else if (dto.getDepartmentName() != null) {
            department = departmentRepo.findByName(dto.getDepartmentName())
                    .orElseThrow(() -> new RuntimeException("Department not found with name: " + dto.getDepartmentName()));
        } else {
            throw new RuntimeException("Department is required. Provide either departmentId or departmentName");
        }

        if (dto.getUserId() != null) {
            user = userRepo.findById(dto.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found with id: " + dto.getUserId()));
        } else {
            throw new RuntimeException("UserId is required");
        }

        ProjectEntity entity = new ProjectEntity();
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        entity.setStartDate(dto.getStartDate());
        entity.setEndDate(dto.getEndDate());
        entity.setTeam(team);
        entity.setDepartment(department);
        entity.setUser(user);
        entity.setProgress(dto.getProgress());
        ProjectEntity saved = projectRepo.save(entity);
        return toDTO(saved);
    }

    // Admin: Update project
    @Override
    @Transactional
    public ProjectDTO updateProject(Long projectId, ProjectDTO dto) {
        ProjectEntity entity = projectRepo.findById(projectId).orElseThrow(() -> new RuntimeException("Project not found with id: " + projectId));
        if (dto.getName() != null) entity.setName(dto.getName());
        if (dto.getDescription() != null) entity.setDescription(dto.getDescription());
        if (dto.getStartDate() != null) entity.setStartDate(dto.getStartDate());
        if (dto.getEndDate() != null) entity.setEndDate(dto.getEndDate());
        
        // Handle team update - check ID first, then name
        if (dto.getTeamId() != null) {
            TeamEntity team = teamRepo.findById(dto.getTeamId()).orElseThrow(() -> new RuntimeException("Team not found with id: " + dto.getTeamId()));
            entity.setTeam(team);
        } else if (dto.getTeamName() != null) {
            TeamEntity team = teamRepo.findByName(dto.getTeamName()).orElseThrow(() -> new RuntimeException("Team not found with name: " + dto.getTeamName()));
            entity.setTeam(team);
        }
        
        // Handle department update - check ID first, then name
        if (dto.getDepartmentId() != null) {
            DepartmentEntity department = departmentRepo.findById(dto.getDepartmentId()).orElseThrow(() -> new RuntimeException("Department not found with id: " + dto.getDepartmentId()));
            entity.setDepartment(department);
        } else if (dto.getDepartmentName() != null) {
            DepartmentEntity department = departmentRepo.findByName(dto.getDepartmentName()).orElseThrow(() -> new RuntimeException("Department not found with name: " + dto.getDepartmentName()));
            entity.setDepartment(department);
        }
        
        if (dto.getUserId() != null) {
            User user = userRepo.findById(dto.getUserId()).orElseThrow(() -> new RuntimeException("User not found with id: " + dto.getUserId()));
            entity.setUser(user);
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
        if (entity == null) {
            return null;
        }
        
        ProjectDTO dto = new ProjectDTO();
        dto.setProjectId(entity.getProjectId());
        dto.setName(entity.getName());
        dto.setDescription(entity.getDescription());
        dto.setStartDate(entity.getStartDate());
        dto.setEndDate(entity.getEndDate());
        dto.setProgress(entity.getProgress());
        
        // Safely handle team relationship
        if (entity.getTeam() != null) {
            dto.setTeamId(entity.getTeam().getTeamId());
            dto.setTeamName(entity.getTeam().getName());
        }
        
        // Safely handle department relationship
        if (entity.getDepartment() != null) {
            dto.setDepartmentId(entity.getDepartment().getDepartmentId());
            dto.setDepartmentName(entity.getDepartment().getName());
        }
        
        // Safely handle user relationship
        if (entity.getUser() != null) {
            dto.setUserId(entity.getUser().getId());
        }
        
        return dto;
    }
} 