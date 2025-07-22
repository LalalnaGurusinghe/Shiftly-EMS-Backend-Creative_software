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

@Service
public class TimesheetServiceImpl implements TimesheetService {
    private final TimesheetRepo timesheetRepo;

    public TimesheetServiceImpl(TimesheetRepo timesheetRepo) {
        this.timesheetRepo = timesheetRepo;
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
    public void deleteTimesheet(Long id, Long userId) {
        Timesheet timesheet = timesheetRepo.findById(id).orElseThrow(() -> new RuntimeException("Timesheet not found"));
        if (!timesheet.getUserId().equals(userId)) {
            throw new RuntimeException("Unauthorized to delete this timesheet");
        }
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

    private TimesheetDTO toDTO(Timesheet entity) {
        TimesheetDTO dto = new TimesheetDTO();
        BeanUtils.copyProperties(entity, dto);
        return dto;
    }
} 