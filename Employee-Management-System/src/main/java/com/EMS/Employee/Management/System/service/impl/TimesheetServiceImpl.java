package com.EMS.Employee.Management.System.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.EMS.Employee.Management.System.dto.TimesheetDTO;
import com.EMS.Employee.Management.System.entity.DepartmentEntity;
import com.EMS.Employee.Management.System.entity.EmployeeEntity;
import com.EMS.Employee.Management.System.entity.TimeSheetStatus;
import com.EMS.Employee.Management.System.entity.Timesheet;
import com.EMS.Employee.Management.System.repo.DepartmentRepo;
import com.EMS.Employee.Management.System.repo.EmployeeRepo;
import com.EMS.Employee.Management.System.repo.TimesheetRepo;
import com.EMS.Employee.Management.System.service.TimesheetService;
@Service
public class TimesheetServiceImpl implements TimesheetService {
    private final TimesheetRepo timesheetRepo;
    private final EmployeeRepo employeeRepo;
    private final DepartmentRepo departmentRepo;

    public TimesheetServiceImpl(TimesheetRepo timesheetRepo, EmployeeRepo employeeRepo, DepartmentRepo departmentRepo) {
        this.timesheetRepo = timesheetRepo;
        this.employeeRepo = employeeRepo;
        this.departmentRepo = departmentRepo;
    }

    @Override
    @Transactional
    public TimesheetDTO create(int employeeId, TimesheetDTO dto) {
        EmployeeEntity employee = employeeRepo.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found"));
        Timesheet entity = new Timesheet();
        entity.setDate(dto.getDate());
        entity.setMode(dto.getMode());
        entity.setActivity(dto.getActivity());
        entity.setHours(dto.getHours());
        entity.setStatus(TimeSheetStatus.PENDING);
        entity.setEmployee(employee);
        Timesheet saved = timesheetRepo.save(entity);
        return toDTO(saved);
    }

    @Override
    public List<TimesheetDTO> getTimesheetsForAdmin(Long adminUserId) {
        DepartmentEntity department = departmentRepo.findByAdmin_Id(adminUserId)
                .stream().findFirst().orElse(null);
        if (department == null)
            return List.of();
        return timesheetRepo.findByEmployee_Department_Id(department.getId())
                .stream().map(this::toDTO).toList();
    }

    @Override
    public List<TimesheetDTO> getAllTimesheets() {
        return timesheetRepo.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Override
    public List<TimesheetDTO> getByEmployeeId(int employeeId) {
        return timesheetRepo.findByEmployee_EmployeeId(employeeId).stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteTimesheet(Long id) {
    Timesheet timesheet = timesheetRepo.findById(id).orElseThrow(() -> new
    RuntimeException("Timesheet not found"));
    timesheetRepo.delete(timesheet);
    }

    @Override
    @Transactional
    public TimesheetDTO updateStatus(Long id, String status) {
    Timesheet timesheet = timesheetRepo.findById(id).orElseThrow(() -> new
    RuntimeException("Timesheet not found"));
    timesheet.setStatus(TimeSheetStatus.valueOf(status));
    Timesheet saved = timesheetRepo.save(timesheet);
    return toDTO(saved);
    }

    @Override
    @Transactional
    public TimesheetDTO updateTimesheet(Long id, TimesheetDTO dto) {
    Timesheet entity = timesheetRepo.findById(id).orElseThrow(() -> new
    RuntimeException("Timesheet not found"));
    if (dto.getDate() != null)
    entity.setDate(dto.getDate());
    if (dto.getMode() != null)
    entity.setMode(dto.getMode());
    if (dto.getActivity() != null)
    entity.setActivity(dto.getActivity());
    if (dto.getHours() != 0)
    entity.setHours(dto.getHours());
    if (dto.getStatus() != null)
    entity.setStatus(TimeSheetStatus.valueOf(dto.getStatus()));
    Timesheet saved = timesheetRepo.save(entity);
    return toDTO(saved);
    }

    private TimesheetDTO toDTO(Timesheet entity) {
        TimesheetDTO dto = new TimesheetDTO();
        if (entity.getEmployee() != null) {
            dto.setEmployeeId(entity.getEmployee().getEmployeeId());
            dto.setDepartmentName(
                    entity.getEmployee().getDepartment() != null ? entity.getEmployee().getDepartment().getName()
                            : null);
            dto.setEmployeeName(entity.getEmployee().getFirstName() != null ? entity.getEmployee().getFirstName() + " " + entity.getEmployee().getLastName() : null);
        }
        dto.setId(entity.getId());
        dto.setDate(entity.getDate());
        dto.setMode(entity.getMode());
        dto.setActivity(entity.getActivity());
        dto.setHours(entity.getHours());
        dto.setStatus(entity.getStatus().name());
        return dto;
    }
}