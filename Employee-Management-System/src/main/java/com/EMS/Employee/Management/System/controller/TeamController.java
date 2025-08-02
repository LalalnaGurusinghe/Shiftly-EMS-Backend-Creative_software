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
import com.EMS.Employee.Management.System.repo.TeamRepo;
import com.EMS.Employee.Management.System.repo.DepartmentRepo;

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
    @Autowired
    private TeamRepo teamRepo;
    @Autowired
    private DepartmentRepo departmentRepo;

    @GetMapping("/all")
    public ResponseEntity<List<TeamDTO>> getAllTeams() {
        List<TeamDTO> teams = teamService.getAllTeams();
        return ResponseEntity.ok(teams);
    }

    // @GetMapping("/by-department/{departmentId}")
    // public ResponseEntity<List<TeamDTO>> getTeamsByDepartment(@PathVariable Long departmentId) {
    //     List<TeamDTO> teams = teamService.getTeamsByDepartment(departmentId);
    //     return ResponseEntity.ok(teams);
    // }

    @GetMapping("/name/{teamId}")
    public ResponseEntity<String> getTeamNameById(@PathVariable Long teamId) {
        return teamRepo.findById(teamId)
                .map(team -> ResponseEntity.ok(team.getName()))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/department-name/{departmentId}")
    public ResponseEntity<String> getDepartmentNameById(@PathVariable Long departmentId) {
        return departmentRepo.findById(departmentId)
                .map(dept -> ResponseEntity.ok(dept.getName()))
                .orElse(ResponseEntity.notFound().build());
    }

    // Removed getOwnTeam endpoint as getEmployeeTeam no longer exists
} 