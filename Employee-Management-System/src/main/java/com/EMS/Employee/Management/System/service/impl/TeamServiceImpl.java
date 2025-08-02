package com.EMS.Employee.Management.System.service.impl;

import com.EMS.Employee.Management.System.dto.TeamDTO;
import com.EMS.Employee.Management.System.entity.DepartmentEntity;
import com.EMS.Employee.Management.System.entity.EmployeeEntity;
import com.EMS.Employee.Management.System.entity.TeamEntity;
import com.EMS.Employee.Management.System.repo.DepartmentRepo;
import com.EMS.Employee.Management.System.repo.EmployeeRepo;
import com.EMS.Employee.Management.System.repo.TeamRepo;
import com.EMS.Employee.Management.System.service.TeamService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TeamServiceImpl implements TeamService {
    private final TeamRepo teamRepo;
    private final DepartmentRepo departmentRepo;
    private final EmployeeRepo employeeRepo;

    public TeamServiceImpl(TeamRepo teamRepo, DepartmentRepo departmentRepo, EmployeeRepo employeeRepo) {
        this.teamRepo = teamRepo;
        this.departmentRepo = departmentRepo;
        this.employeeRepo = employeeRepo;
    }

    @Override
    public List<TeamDTO> getAllTeams() {
        return teamRepo.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    // @Override
    // public List<TeamDTO> getTeamsByDepartment(Long departmentId) {
    //     return teamRepo.findByDepartment_DepartmentId(departmentId).stream().map(this::toDTO).collect(Collectors.toList());
    // }

    @Override
    public String getTeamNameById(Long teamId) {
        return teamRepo.findById(teamId).map(TeamEntity::getName).orElse(null);
    }

    @Override
    public String getDepartmentNameById(Long departmentId) {
        return departmentRepo.findById(departmentId).map(DepartmentEntity::getName).orElse(null);
    }

    // Helper: Entity to DTO
    private TeamDTO toDTO(TeamEntity entity) {
        TeamDTO dto = new TeamDTO();
        BeanUtils.copyProperties(entity, dto);
        dto.setDepartmentId(entity.getDepartment() != null ? entity.getDepartment().getId() : null);
        dto.setDepartmentName(entity.getDepartment() != null ? entity.getDepartment().getName() : null);
        return dto;
    }
} 