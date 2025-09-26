package com.EMS.Employee.Management.System.controller;

import com.EMS.Employee.Management.System.dto.ProjectDTO;
import com.EMS.Employee.Management.System.entity.EmployeeEntity;
import com.EMS.Employee.Management.System.repo.EmployeeRepo;
import com.EMS.Employee.Management.System.repo.UserRepo;
import com.EMS.Employee.Management.System.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/v1/shiftly/ems/projects")
public class ProjectController {
    @Autowired
    private ProjectService projectService;
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private EmployeeRepo employeeRepo;

    // Admin: Create project
    @PostMapping("/add")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProjectDTO> createProject(@RequestBody ProjectDTO dto) {
        ProjectDTO result = projectService.createProject(dto);
        return ResponseEntity.ok(result);
    }

    // Admin: Update project
    @PutMapping("/update/{projectId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProjectDTO> updateProject(@PathVariable Long projectId, @RequestBody ProjectDTO dto) {
        ProjectDTO result = projectService.updateProject(projectId, dto);
        return ResponseEntity.ok(result);
    }

    // Admin: Delete project
    @DeleteMapping("/delete/{projectId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteProject(@PathVariable Long projectId) {
        projectService.deleteProject(projectId);
        return ResponseEntity.noContent().build();
    }

    // Admin & Employee: View all projects
    @GetMapping("/all")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<ProjectDTO>> getAllProjects() {
        List<ProjectDTO> projects = projectService.getAllProjects();
        return ResponseEntity.ok(projects);
    }

    // // Employee: View own projects (by assigned team)
    // @GetMapping("/my")
    // @PreAuthorize("hasRole('USER')")
    // public ResponseEntity<List<ProjectDTO>> getOwnProjects(Principal principal) {
    //     String username = principal.getName();
    //     Long userId = userRepo.findByUsername(username).map(u -> u.getId()).orElse(null);
    //     if (userId == null) return ResponseEntity.status(404).build();
    //     EmployeeEntity employee = employeeRepo.findByUser_Id(userId);
    //     if (employee == null) return ResponseEntity.status(404).build();
    //     String teamName = employee.getTeamName();
    //     if (teamName == null || teamName.isEmpty()) return ResponseEntity.ok(List.of());
    //     List<ProjectDTO> allProjects = projectService.getAllProjects();
    //     List<ProjectDTO> myProjects = allProjects.stream()
    //         .filter(p -> teamName.equals(p.getTeamName()))
    //         .toList();
    //     return ResponseEntity.ok(myProjects);
    // }
} 