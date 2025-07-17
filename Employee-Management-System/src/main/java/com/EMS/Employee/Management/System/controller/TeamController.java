package com.EMS.Employee.Management.System.controller;

import com.EMS.Employee.Management.System.dto.TeamDTO;
import com.EMS.Employee.Management.System.service.TeamService;
import com.EMS.Employee.Management.System.repo.UserRepo;
import com.EMS.Employee.Management.System.repo.EmployeeRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;
import java.util.List;
import com.EMS.Employee.Management.System.entity.EmployeeEntity;

@RestController
@RequestMapping("/api/v1/shiftly/ems/teams")
@CrossOrigin(origins = "http://localhost:3000")
public class TeamController {
    @Autowired
    private TeamService teamService;
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private EmployeeRepo employeeRepo;

    // Admin: Create team
    @PostMapping("/add")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<TeamDTO> createTeam(@RequestBody TeamDTO dto) {
        TeamDTO result = teamService.createTeam(dto);
        return ResponseEntity.ok(result);
    }

    // Admin: Get teams by department
    @GetMapping("/by-department/{departmentId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<TeamDTO>> getTeamsByDepartment(@PathVariable Long departmentId) {
        List<TeamDTO> teams = teamService.getTeamsByDepartment(departmentId);
        return ResponseEntity.ok(teams);
    }

    // Admin: Assign employee to team
    @PutMapping("/assign/{employeeId}/{teamId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<TeamDTO> assignEmployeeToTeam(@PathVariable Long employeeId, @PathVariable Long teamId) {
        TeamDTO result = teamService.assignEmployeeToTeam(employeeId, teamId);
        return ResponseEntity.ok(result);
    }

    // Employee: View own team
    @GetMapping("/my")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<TeamDTO> getOwnTeam(Principal principal) {
        String username = principal.getName();
        Long userId = userRepo.findByUsername(username).map(u -> u.getId()).orElse(null);
        if (userId == null) return ResponseEntity.status(404).build();
        EmployeeEntity employee = employeeRepo.findByUser_Id(userId);
        if (employee == null) return ResponseEntity.status(404).build();
        TeamDTO team = teamService.getEmployeeTeam((long) employee.getEmployeeId());
        if (team == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(team);
    }
} 