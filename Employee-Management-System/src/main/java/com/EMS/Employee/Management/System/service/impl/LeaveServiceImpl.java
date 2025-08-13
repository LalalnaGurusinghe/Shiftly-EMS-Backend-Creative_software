package com.EMS.Employee.Management.System.service.impl;

import com.EMS.Employee.Management.System.dto.LeaveDTO;
import com.EMS.Employee.Management.System.entity.DepartmentEntity;
import com.EMS.Employee.Management.System.entity.EmployeeEntity;
import com.EMS.Employee.Management.System.entity.LeaveEntity;
import com.EMS.Employee.Management.System.entity.LeaveStatus;
import com.EMS.Employee.Management.System.repo.EmployeeRepo;
import com.EMS.Employee.Management.System.repo.LeaveRepo;
import com.EMS.Employee.Management.System.repo.DepartmentRepo;
import com.EMS.Employee.Management.System.service.LeaveService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class LeaveServiceImpl implements LeaveService {
    private final LeaveRepo leaveRepo;
    private final EmployeeRepo employeeRepo;
    private final DepartmentRepo departmentRepo;

    public LeaveServiceImpl(LeaveRepo leaveRepo, EmployeeRepo employeeRepo, DepartmentRepo departmentRepo) {
        this.leaveRepo = leaveRepo;
        this.employeeRepo = employeeRepo;
        this.departmentRepo = departmentRepo;
    }

    @Override
    @Transactional
    public LeaveDTO applyLeave(int employeeId, LeaveDTO dto) {
        EmployeeEntity employee = employeeRepo.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found"));
        EmployeeEntity coverPerson = employeeRepo.findById(dto.getCoverPersonId())
                .orElseThrow(() -> new RuntimeException("Employee not found"));
        LeaveEntity entity = new LeaveEntity();
        entity.setEmployee(employee);
        entity.setLeaveType(dto.getLeaveType());
        entity.setLeaveFrom(dto.getLeaveFrom());
        entity.setLeaveTo(dto.getLeaveTo());
        entity.setCoverPerson(coverPerson);
        entity.setReason(dto.getReason());
        entity.setStatus(LeaveStatus.PENDING);
        LeaveEntity saved = leaveRepo.save(entity);
        return toDTO(saved);
    }

    @Override
    public List<LeaveDTO> getLeavesForAdmin(Long adminUserId) {
        DepartmentEntity department = departmentRepo.findByAdmin_Id(adminUserId)
                .stream().findFirst().orElse(null);
        if (department == null)
            return List.of();
        return leaveRepo.findByEmployee_Department_Id(department.getId())
                .stream().map(this::toDTO).toList();
    }

    @Override
    public List<LeaveDTO> getAllLeaves() {
        return leaveRepo.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Override
    public List<LeaveDTO> getByEmployeeId(int employeeId) {
        return leaveRepo.findByEmployee_EmployeeId(employeeId).stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteLeave(Long id) {
        LeaveEntity leave = leaveRepo.findById(id).orElseThrow(() -> new RuntimeException("Leave not found"));
        leaveRepo.delete(leave);
    }

    @Override
    @Transactional
    public LeaveDTO updateStatus(Long id, String status) {
        LeaveEntity leave = leaveRepo.findById(id).orElseThrow(() -> new RuntimeException("Timesheet not found"));
        leave.setStatus(LeaveStatus.valueOf(status));
        LeaveEntity saved = leaveRepo.save(leave);
        return toDTO(saved);
    }

    @Override
    @Transactional
    public LeaveDTO updateLeave(Long id, LeaveDTO dto) {
        LeaveEntity entity = leaveRepo.findById(id).orElseThrow(() -> new RuntimeException("Leave not found"));
        if (dto.getLeaveType() != null)
            entity.setLeaveType(dto.getLeaveType());
        if (dto.getLeaveFrom() != null)
            entity.setLeaveFrom(dto.getLeaveFrom());
        if (dto.getLeaveTo() != null)
            entity.setLeaveTo(dto.getLeaveTo());
        if(dto.getCoverPersonId() != 0) {
            EmployeeEntity coverPerson = employeeRepo.findById(dto.getCoverPersonId())
                    .orElseThrow(() -> new RuntimeException("Cover person not found"));
            entity.setCoverPerson(coverPerson);
        }
        if (dto.getReason() != null)
            entity.setReason(dto.getReason());
        if (dto.getStatus() != null)
            entity.setStatus(LeaveStatus.valueOf(dto.getStatus()));
        LeaveEntity saved = leaveRepo.save(entity);
        return toDTO(saved);
    }

    private LeaveDTO toDTO(LeaveEntity entity) {
        LeaveDTO dto = new LeaveDTO();
        if (entity.getEmployee() != null) {
            dto.setEmployeeId(entity.getEmployee().getEmployeeId());
            dto.setDepartmentName(
                    entity.getEmployee().getDepartment() != null ? entity.getEmployee().getDepartment().getName()
                            : null);
            dto.setEmployeeName(entity.getEmployee().getFirstName() != null
                    ? entity.getEmployee().getFirstName() + " " + entity.getEmployee().getLastName()
                    : null);
        }
        if (entity.getCoverPerson() != null) {
            dto.setCoverPersonId(entity.getCoverPerson().getEmployeeId());
            dto.setCoverPersonName(entity.getCoverPerson().getFirstName() != null &&
                    entity.getCoverPerson().getLastName() != null
                    ? entity.getCoverPerson().getFirstName() + " " + entity.getCoverPerson().getLastName()
                    : null);
        }
        dto.setId(entity.getId());
        dto.setLeaveType(entity.getLeaveType());
        dto.setLeaveFrom(entity.getLeaveFrom());
        dto.setLeaveTo(entity.getLeaveTo());
        dto.setReason(entity.getReason());
        dto.setStatus(entity.getStatus().name());
        return dto;
    }
}