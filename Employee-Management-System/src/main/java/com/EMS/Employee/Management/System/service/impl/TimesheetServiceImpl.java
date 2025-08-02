package com.EMS.Employee.Management.System.service.impl;

import com.EMS.Employee.Management.System.dto.TimesheetDTO;
import com.EMS.Employee.Management.System.entity.Timesheet;
import com.EMS.Employee.Management.System.repo.TimesheetRepo;
import com.EMS.Employee.Management.System.service.TimesheetService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;
import com.EMS.Employee.Management.System.entity.EmployeeEntity;
import com.EMS.Employee.Management.System.repo.EmployeeRepo;

@Service
public class TimesheetServiceImpl implements TimesheetService {
    private final TimesheetRepo timesheetRepo;
    private final EmployeeRepo employeeRepo;

    public TimesheetServiceImpl(TimesheetRepo timesheetRepo, EmployeeRepo employeeRepo) {
        this.timesheetRepo = timesheetRepo;
        this.employeeRepo = employeeRepo;
    }

    @Override
    @Transactional
    public TimesheetDTO createTimesheet(TimesheetDTO dto) {
        Timesheet entity = new Timesheet();
        BeanUtils.copyProperties(dto, entity);
        entity.setStatus("PENDING");
        Timesheet saved = timesheetRepo.save(entity);
        TimesheetDTO result = new TimesheetDTO();
        BeanUtils.copyProperties(saved, result);
        return result;
    }

    @Override
    public List<TimesheetDTO> getAllTimesheets() {
        return timesheetRepo.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Override
    public List<TimesheetDTO> getTimesheetsByUserId(Long userId) {
        return timesheetRepo.findByUserId(userId).stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteTimesheet(Long id) {
        Timesheet timesheet = timesheetRepo.findById(id).orElseThrow(() -> new RuntimeException("Timesheet not found"));
        timesheetRepo.delete(timesheet);
    }

    @Override
    @Transactional
    public TimesheetDTO updateStatus(Long id, String status) {
        Timesheet timesheet = timesheetRepo.findById(id).orElseThrow(() -> new RuntimeException("Timesheet not found"));
        timesheet.setStatus(status);
        Timesheet saved = timesheetRepo.save(timesheet);
        return toDTO(saved);
    }

    @Override
    @Transactional
    public TimesheetDTO updateTimesheet(Long id, TimesheetDTO dto) {
        Timesheet entity = timesheetRepo.findById(id).orElseThrow(() -> new RuntimeException("Timesheet not found"));
        if (dto.getDate() != null)
            entity.setDate(dto.getDate());
        if (dto.getMode() != null)
            entity.setMode(dto.getMode());
        if (dto.getActivity() != null)
            entity.setActivity(dto.getActivity());
        if (dto.getHours() != 0)
            entity.setHours(dto.getHours());
        if (dto.getStatus() != null)
            entity.setStatus(dto.getStatus());
        Timesheet saved = timesheetRepo.save(entity);
        return toDTO(saved);
    }

    private TimesheetDTO toDTO(Timesheet entity) {
        TimesheetDTO dto = new TimesheetDTO();
        BeanUtils.copyProperties(entity, dto);
        // Set departmentName using EmployeeEntity
        return dto;
    }
}