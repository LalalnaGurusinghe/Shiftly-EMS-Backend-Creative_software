package com.EMS.Employee.Management.System.service;

import com.EMS.Employee.Management.System.dto.TeamDTO;
import java.util.List;

public interface TeamService {
    // Admin
    TeamDTO createTeam(TeamDTO dto);
    List<TeamDTO> getTeamsByDepartment(Long departmentId);
    TeamDTO assignEmployeeToTeam(Long employeeId, Long teamId);

    // Employee
    TeamDTO getEmployeeTeam(Long employeeId);
} 