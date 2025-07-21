package com.EMS.Employee.Management.System.controller;

import com.EMS.Employee.Management.System.dto.TeamMemberDTO;
import com.EMS.Employee.Management.System.entity.EmployeeEntity;
import com.EMS.Employee.Management.System.repo.EmployeeRepo;
import com.EMS.Employee.Management.System.repo.UserRepo;
import com.EMS.Employee.Management.System.service.TeamMemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/v1/shiftly/ems/team-members")
@CrossOrigin(origins = "http://localhost:3000")
public class TeamMemberController {
    @Autowired
    private TeamMemberService teamMemberService;
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private EmployeeRepo employeeRepo;

    // Admin & Employee: View team members by teamId
    @GetMapping("/by-team/{teamId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER', 'SUPER_ADMIN')")
    public ResponseEntity<List<TeamMemberDTO>> getTeamMembersByTeamId(@PathVariable Long teamId) {
        List<TeamMemberDTO> members = teamMemberService.getTeamMembersByTeamId(teamId);
        return ResponseEntity.ok(members);
    }

    // Employee: View own team members
    @GetMapping("/my-team")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<TeamMemberDTO>> getOwnTeamMembers(Principal principal) {
        String username = principal.getName();
        Long userId = userRepo.findByUsername(username).map(u -> u.getId()).orElse(null);
        if (userId == null) return ResponseEntity.status(404).build();
        EmployeeEntity employee = employeeRepo.findByUser_Id(userId);
        if (employee == null) return ResponseEntity.status(404).build();
        // No team info, so just return empty list
        return ResponseEntity.ok(List.of());
    }

    // Admin: Add member to team
    @PostMapping("/add")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> addMemberToTeam(@RequestParam Long teamId, @RequestParam Integer employeeId) {
        teamMemberService.addMemberToTeam(teamId, employeeId);
        return ResponseEntity.ok().build();
    }

    // Admin: Remove member from team
    @DeleteMapping("/remove")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> removeMemberFromTeam(@RequestParam Long teamId, @RequestParam Integer employeeId) {
        teamMemberService.removeMemberFromTeam(teamId, employeeId);
        return ResponseEntity.noContent().build();
    }
} 