package com.EMS.Employee.Management.System.service.impl;

import com.EMS.Employee.Management.System.dto.LeaveDTO;
import com.EMS.Employee.Management.System.entity.EmployeeEntity;
import com.EMS.Employee.Management.System.entity.LeaveEntity;
import com.EMS.Employee.Management.System.entity.LeaveStatus;
import com.EMS.Employee.Management.System.entity.User;
import com.EMS.Employee.Management.System.repo.EmployeeRepo;
import com.EMS.Employee.Management.System.repo.LeaveRepo;
import com.EMS.Employee.Management.System.repo.UserRepo;
import com.EMS.Employee.Management.System.service.LeaveService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class LeaveServiceImpl implements LeaveService {
    private final LeaveRepo leaveRepo;
    private final UserRepo userRepo;
    private final EmployeeRepo employeeRepo;

    public LeaveServiceImpl(LeaveRepo leaveRepo, UserRepo userRepo, EmployeeRepo employeeRepo) {
        this.leaveRepo = leaveRepo;
        this.userRepo = userRepo;
        this.employeeRepo = employeeRepo;
    }

    // Employee: Apply for leave
    @Override
    @Transactional
    public LeaveDTO applyLeave(LeaveDTO leaveDTO, String username) {
        User user = userRepo.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));
        LeaveEntity entity = new LeaveEntity();
        BeanUtils.copyProperties(leaveDTO, entity);
        entity.setUser(user);
        // Remove fileData logic since DTO does not have it
        // Map coverPerson by name
        if (leaveDTO.getCoverPersonName() != null && !leaveDTO.getCoverPersonName().isEmpty()) {
            String[] names = leaveDTO.getCoverPersonName().split(" ", 2);
            if (names.length < 2) throw new RuntimeException("Cover person name must include first and last name");
            EmployeeEntity cover = employeeRepo.findByFirstNameAndLastName(names[0], names[1]);
            if (cover == null) throw new RuntimeException("Cover person not found");
            entity.setCoverPerson(cover);
        }
        // Map reportTo by name
        if (leaveDTO.getReportToName() != null && !leaveDTO.getReportToName().isEmpty()) {
            String[] names = leaveDTO.getReportToName().split(" ", 2);
            if (names.length < 2) throw new RuntimeException("Report to name must include first and last name");
            EmployeeEntity reportTo = employeeRepo.findByFirstNameAndLastName(names[0], names[1]);
            if (reportTo == null) throw new RuntimeException("Report to not found");
            entity.setReportTo(reportTo);
        }
        entity.setLeaveStatus(LeaveStatus.PENDING);
        LeaveEntity saved = leaveRepo.save(entity);
        return toDTO(saved);
    }

    // Employee: View own leaves
    @Override
    public List<LeaveDTO> getOwnLeaves(String username) {
        User user = userRepo.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));
        return leaveRepo.findByUser_Id(user.getId()).stream().map(this::toDTO).collect(Collectors.toList());
    }

    // Employee: Update own leave
    @Override
    @Transactional
    public LeaveDTO updateOwnLeave(Long leaveId, LeaveDTO leaveDTO, String username) {
        User user = userRepo.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));
        LeaveEntity entity = leaveRepo.findById(leaveId)
            .orElseThrow(() -> new RuntimeException("Leave not found"));
        if (!entity.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Unauthorized");
        }
        if (leaveDTO.getLeaveType() != null) entity.setLeaveType(leaveDTO.getLeaveType());
        if (leaveDTO.getLeaveFrom() != null) entity.setLeaveFrom(leaveDTO.getLeaveFrom());
        if (leaveDTO.getLeaveTo() != null) entity.setLeaveTo(leaveDTO.getLeaveTo());
        if (leaveDTO.getReason() != null) entity.setReason(leaveDTO.getReason());
        // Remove duration, coverPersonId, reportToId logic
        LeaveEntity saved = leaveRepo.save(entity);
        return toDTO(saved);
    }

    // Employee: Delete own leave
    @Override
    @Transactional
    public void deleteOwnLeave(Long leaveId, String username) {
        User user = userRepo.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));
        LeaveEntity entity = leaveRepo.findById(leaveId)
            .orElseThrow(() -> new RuntimeException("Leave not found"));
        if (!entity.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Unauthorized");
        }
        leaveRepo.deleteById(leaveId);
    }

    // Admin: View all leaves
    @Override
    public List<LeaveDTO> getAllLeaves() {
        return leaveRepo.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    // Admin: Approve/Reject/Update status
    @Override
    @Transactional
    public LeaveDTO updateLeaveStatus(Long leaveId, String status) {
        LeaveEntity entity = leaveRepo.findById(leaveId)
            .orElseThrow(() -> new RuntimeException("Leave not found"));
        entity.setLeaveStatus(LeaveStatus.valueOf(status));
        LeaveEntity saved = leaveRepo.save(entity);
        return toDTO(saved);
    }

    // Helper: Entity to DTO
    private LeaveDTO toDTO(LeaveEntity entity) {
        LeaveDTO dto = new LeaveDTO();
        BeanUtils.copyProperties(entity, dto);
        // Remove fileData, employeeFirstName, username, coverPersonId, reportToId logic
        dto.setUserId(entity.getUser() != null ? entity.getUser().getId() : null);
        dto.setCoverPersonName(entity.getCoverPerson() != null ? entity.getCoverPerson().getFirstName() + " " + entity.getCoverPerson().getLastName() : null);
        dto.setReportToName(entity.getReportTo() != null ? entity.getReportTo().getFirstName() + " " + entity.getReportTo().getLastName() : null);
        dto.setLeaveStatus(entity.getLeaveStatus() != null ? entity.getLeaveStatus().name() : null);
        // Set departmentName using EmployeeEntity
        if (entity.getUser() != null) {
            EmployeeEntity employee = employeeRepo.findByUser_Id(entity.getUser().getId());
            if (employee != null) {
                dto.setDepartmentName(employee.getDepartment());
            }
        }
        return dto;
    }
} 