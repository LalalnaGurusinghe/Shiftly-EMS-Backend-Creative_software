package com.EMS.Employee.Management.System.service;

import com.EMS.Employee.Management.System.dto.TeamDTO;
import java.util.List;

public interface TeamService {
    // Admin
    List<TeamDTO> getAllTeams();
    // List<TeamDTO> getTeamsByDepartment(Long departmentId);
    String getTeamNameById(Long teamId);
    String getDepartmentNameById(Long departmentId);
} 