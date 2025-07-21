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

    // Admin: Create team
    @Override
    @Transactional
    public TeamDTO createTeam(TeamDTO dto) {
        DepartmentEntity department = departmentRepo.findById(dto.getDepartmentId())
                .orElseThrow(() -> new RuntimeException("Department not found"));
        TeamEntity entity = new TeamEntity();
        BeanUtils.copyProperties(dto, entity);
        entity.setDepartment(department);
        TeamEntity saved = teamRepo.save(entity);
        return toDTO(saved);
    }

    // Admin: Get teams by department
    @Override
    public List<TeamDTO> getTeamsByDepartment(Long departmentId) {
        return teamRepo.findByDepartment_DepartmentId(departmentId).stream().map(this::toDTO).collect(Collectors.toList());
    }

    // Admin: Assign employee to team (one team per employee)
    @Override
    @Transactional
    public TeamDTO assignEmployeeToTeam(Long employeeId, Long teamId) {
        // Team assignment is no longer supported
        throw new UnsupportedOperationException("Team assignment is not supported.");
    }

    @Override
    public TeamDTO getEmployeeTeam(Long employeeId) {
        // Team info is no longer available
        return null;
    }

    // Helper: Entity to DTO
    private TeamDTO toDTO(TeamEntity entity) {
        TeamDTO dto = new TeamDTO();
        BeanUtils.copyProperties(entity, dto);
        dto.setDepartmentId(entity.getDepartment() != null ? entity.getDepartment().getDepartmentId() : null);
        dto.setDepartmentName(entity.getDepartment() != null ? entity.getDepartment().getName() : null);
        return dto;
    }
} 