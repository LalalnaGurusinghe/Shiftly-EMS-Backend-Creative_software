package com.EMS.Employee.Management.System.service;

import com.EMS.Employee.Management.System.dto.TeamMemberDTO;
import java.util.List;

public interface TeamMemberService {
    List<TeamMemberDTO> getTeamMembersByTeamId(Long teamId);
    List<TeamMemberDTO> getTeamMembersByEmployeeId(Integer employeeId);
    void addMemberToTeam(Long teamId, Integer employeeId);
    void removeMemberFromTeam(Long teamId, Integer employeeId);
} 