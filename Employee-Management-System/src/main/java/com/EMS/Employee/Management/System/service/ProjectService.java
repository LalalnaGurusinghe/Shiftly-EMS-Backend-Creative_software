package com.EMS.Employee.Management.System.service;

import com.EMS.Employee.Management.System.dto.ProjectDTO;
import java.util.List;

public interface ProjectService {
    // Admin
    ProjectDTO createProject(ProjectDTO dto);
    ProjectDTO updateProject(Long projectId, ProjectDTO dto);
    void deleteProject(Long projectId);
    List<ProjectDTO> getAllProjects();

    // Employee
    List<ProjectDTO> getProjectsByTeamId(Long teamId);
} 