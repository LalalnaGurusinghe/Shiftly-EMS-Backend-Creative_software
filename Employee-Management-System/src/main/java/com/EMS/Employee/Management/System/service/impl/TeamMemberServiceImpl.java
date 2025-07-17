package com.EMS.Employee.Management.System.service.impl;

import com.EMS.Employee.Management.System.dto.TeamMemberDTO;
import com.EMS.Employee.Management.System.entity.TeamMemberEntity;
import com.EMS.Employee.Management.System.entity.TeamEntity;
import com.EMS.Employee.Management.System.entity.EmployeeEntity;
import com.EMS.Employee.Management.System.repo.TeamMemberRepo;
import com.EMS.Employee.Management.System.repo.TeamRepo;
import com.EMS.Employee.Management.System.repo.EmployeeRepo;
import com.EMS.Employee.Management.System.service.TeamMemberService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TeamMemberServiceImpl implements TeamMemberService {
    private final TeamMemberRepo teamMemberRepo;
    private final TeamRepo teamRepo;
    private final EmployeeRepo employeeRepo;

    public TeamMemberServiceImpl(TeamMemberRepo teamMemberRepo, TeamRepo teamRepo, EmployeeRepo employeeRepo) {
        this.teamMemberRepo = teamMemberRepo;
        this.teamRepo = teamRepo;
        this.employeeRepo = employeeRepo;
    }

    @Override
    public List<TeamMemberDTO> getTeamMembersByTeamId(Long teamId) {
        return teamMemberRepo.findByTeam_TeamId(teamId).stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Override
    public List<TeamMemberDTO> getTeamMembersByEmployeeId(Integer employeeId) {
        return teamMemberRepo.findByEmployee_EmployeeId(employeeId).stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Override
    public void addMemberToTeam(Long teamId, Integer employeeId) {
        TeamEntity team = teamRepo.findById(teamId).orElseThrow(() -> new RuntimeException("Team not found"));
        EmployeeEntity employee = employeeRepo.findById(employeeId).orElseThrow(() -> new RuntimeException("Employee not found"));
        // Prevent duplicate membership
        boolean exists = teamMemberRepo.findByTeam_TeamId(teamId).stream().anyMatch(m -> m.getEmployee().getEmployeeId() == employeeId);
        if (!exists) {
            TeamMemberEntity member = new TeamMemberEntity();
            member.setTeam(team);
            member.setEmployee(employee);
            teamMemberRepo.save(member);
        }
    }

    @Override
    public void removeMemberFromTeam(Long teamId, Integer employeeId) {
        List<TeamMemberEntity> members = teamMemberRepo.findByTeam_TeamId(teamId);
        for (TeamMemberEntity member : members) {
            if (member.getEmployee().getEmployeeId() == employeeId) {
                teamMemberRepo.delete(member);
                break;
            }
        }
    }

    private TeamMemberDTO toDTO(TeamMemberEntity entity) {
        TeamMemberDTO dto = new TeamMemberDTO();
        BeanUtils.copyProperties(entity, dto);
        dto.setTeamId(entity.getTeam() != null ? entity.getTeam().getTeamId() : null);
        dto.setEmployeeId(entity.getEmployee() != null ? entity.getEmployee().getEmployeeId() : null);
        if (entity.getEmployee() != null) {
            dto.setEmployeeName(entity.getEmployee().getFirstName() + " " + entity.getEmployee().getLastName());
        }
        return dto;
    }
} 