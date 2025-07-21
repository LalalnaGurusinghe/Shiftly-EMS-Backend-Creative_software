package com.EMS.Employee.Management.System.service.impl;

import com.EMS.Employee.Management.System.dto.TimesheetDTO;
import com.EMS.Employee.Management.System.entity.*;
import com.EMS.Employee.Management.System.repo.*;
import com.EMS.Employee.Management.System.service.TimesheetService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TimesheetServiceImpl implements TimesheetService {
    private final TimesheetRepo timesheetRepo;
    private final UserRepo userRepo;
    private final EmployeeRepo employeeRepo;
    private final ProjectRepo projectRepo;

    public TimesheetServiceImpl(TimesheetRepo timesheetRepo, UserRepo userRepo, EmployeeRepo employeeRepo, ProjectRepo projectRepo) {
        this.timesheetRepo = timesheetRepo;
        this.userRepo = userRepo;
        this.employeeRepo = employeeRepo;
        this.projectRepo = projectRepo;
    }

    // Employee: Submit timesheet
    @Override
    @Transactional
    public TimesheetDTO submitTimesheet(TimesheetDTO dto, String username) {
        User user = userRepo.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));
        EmployeeEntity employee = employeeRepo.findByUser_Id(user.getId());
        if (employee == null) throw new RuntimeException("Employee not found");
        // Team info is removed; cannot assign project by team
        throw new UnsupportedOperationException("Project assignment by team is not supported.");
    }

    // Employee: View own timesheets
    @Override
    public List<TimesheetDTO> getOwnTimesheets(String username) {
        User user = userRepo.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));
        EmployeeEntity employee = employeeRepo.findByUser_Id(user.getId());
        if (employee == null) throw new RuntimeException("Employee not found");
        return timesheetRepo.findByEmployee_EmployeeId(employee.getEmployeeId()).stream().map(this::toDTO).collect(Collectors.toList());
    }

    // Admin: View all timesheets
    @Override
    public List<TimesheetDTO> getAllTimesheets() {
        return timesheetRepo.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    // Admin: Approve timesheet
    @Override
    @Transactional
    public TimesheetDTO approveTimesheet(Long timesheetId) {
        Timesheet entity = timesheetRepo.findById(timesheetId).orElseThrow(() -> new RuntimeException("Timesheet not found"));
        entity.setStatus(TimesheetStatus.APPROVED);
        Timesheet saved = timesheetRepo.save(entity);
        return toDTO(saved);
    }

    // Admin: Reject timesheet
    @Override
    @Transactional
    public TimesheetDTO rejectTimesheet(Long timesheetId) {
        Timesheet entity = timesheetRepo.findById(timesheetId).orElseThrow(() -> new RuntimeException("Timesheet not found"));
        entity.setStatus(TimesheetStatus.REJECTED);
        Timesheet saved = timesheetRepo.save(entity);
        return toDTO(saved);
    }

    private TimesheetDTO toDTO(Timesheet entity) {
        TimesheetDTO dto = new TimesheetDTO();
        BeanUtils.copyProperties(entity, dto);
        if (entity.getEmployee() != null) {
            dto.setUserId(entity.getEmployee().getUser() != null ? entity.getEmployee().getUser().getId() : null);
            dto.setEmployeeFirstName(entity.getEmployee().getFirstName());
        }
        return dto;
    }
} 