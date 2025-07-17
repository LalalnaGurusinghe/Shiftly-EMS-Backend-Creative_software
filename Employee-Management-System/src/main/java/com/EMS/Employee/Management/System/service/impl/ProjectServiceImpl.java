package com.EMS.Employee.Management.System.service.impl;

import com.EMS.Employee.Management.System.dto.ProjectDTO;
import com.EMS.Employee.Management.System.entity.ProjectEntity;
import com.EMS.Employee.Management.System.entity.TeamEntity;
import com.EMS.Employee.Management.System.repo.ProjectRepo;
import com.EMS.Employee.Management.System.repo.TeamRepo;
import com.EMS.Employee.Management.System.service.ProjectService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProjectServiceImpl implements ProjectService {
    private final ProjectRepo projectRepo;
    private final TeamRepo teamRepo;

    public ProjectServiceImpl(ProjectRepo projectRepo, TeamRepo teamRepo) {
        this.projectRepo = projectRepo;
        this.teamRepo = teamRepo;
    }

    // Admin: Create project
    @Override
    @Transactional
    public ProjectDTO createProject(ProjectDTO dto) {
        TeamEntity team = teamRepo.findById(dto.getTeamId()).orElseThrow(() -> new RuntimeException("Team not found"));
        ProjectEntity entity = new ProjectEntity();
        BeanUtils.copyProperties(dto, entity);
        entity.setTeam(team);
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
        return dto;
    }
} 