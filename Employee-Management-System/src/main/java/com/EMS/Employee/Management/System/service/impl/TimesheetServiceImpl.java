package com.EMS.Employee.Management.System.service.impl;

import com.EMS.Employee.Management.System.dto.TimesheetDTO;
import com.EMS.Employee.Management.System.entity.Timesheet;
import com.EMS.Employee.Management.System.entity.TimesheetStatus;
import com.EMS.Employee.Management.System.entity.User;
import com.EMS.Employee.Management.System.repo.TimesheetRepo;
import com.EMS.Employee.Management.System.repo.UserRepo;
import com.EMS.Employee.Management.System.service.TimesheetService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TimesheetServiceImpl implements TimesheetService {
    private final TimesheetRepo timesheetRepo;
    private final UserRepo userRepo;

    public TimesheetServiceImpl(TimesheetRepo timesheetRepo, UserRepo userRepo) {
        this.timesheetRepo = timesheetRepo;
        this.userRepo = userRepo;
    }

    @Override
    @Transactional
    public TimesheetDTO submitTimesheet(TimesheetDTO dto, String username) {
        User user = userRepo.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        if (timesheetRepo.existsByUserIdAndDate(user.getId(), dto.getDate())) {
            throw new RuntimeException("Timesheet for this date already exists");
        }
        Timesheet timesheet = new Timesheet();
        timesheet.setUser(user);
        timesheet.setDate(dto.getDate());
        timesheet.setHoursWorked(dto.getHoursWorked());
        timesheet.setDescription(dto.getDescription());
        timesheet.setStatus(TimesheetStatus.PENDING);
        timesheet.setSubmittedAt(LocalDateTime.now());
        Timesheet saved = timesheetRepo.save(timesheet);
        return toDTO(saved);
    }

    @Override
    public List<TimesheetDTO> getUserTimesheets(String username) {
        User user = userRepo.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return timesheetRepo.findByUserId(user.getId()).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<TimesheetDTO> getAllTimesheets() {
        return timesheetRepo.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public TimesheetDTO approveTimesheet(Long id, String adminUsername) {
        Timesheet timesheet = timesheetRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Timesheet not found"));
        if (timesheet.getStatus() != TimesheetStatus.PENDING) {
            throw new RuntimeException("Only pending timesheets can be approved");
        }
        timesheet.setStatus(TimesheetStatus.APPROVED);
        timesheet.setApprovedAt(LocalDateTime.now());
        Timesheet saved = timesheetRepo.save(timesheet);
        return toDTO(saved);
    }

    @Override
    @Transactional
    public TimesheetDTO rejectTimesheet(Long id, String adminUsername) {
        Timesheet timesheet = timesheetRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Timesheet not found"));
        if (timesheet.getStatus() != TimesheetStatus.PENDING) {
            throw new RuntimeException("Only pending timesheets can be rejected");
        }
        timesheet.setStatus(TimesheetStatus.REJECTED);
        timesheet.setApprovedAt(LocalDateTime.now());
        Timesheet saved = timesheetRepo.save(timesheet);
        return toDTO(saved);
    }

    private TimesheetDTO toDTO(Timesheet t) {
        TimesheetDTO dto = new TimesheetDTO();
        dto.setId(t.getId());
        dto.setUserId(t.getUser().getId());
        dto.setUsername(t.getUser().getUsername());
        dto.setDate(t.getDate());
        dto.setHoursWorked(t.getHoursWorked());
        dto.setDescription(t.getDescription());
        dto.setStatus(t.getStatus());
        dto.setSubmittedAt(t.getSubmittedAt());
        dto.setApprovedAt(t.getApprovedAt());
        return dto;
    }
}