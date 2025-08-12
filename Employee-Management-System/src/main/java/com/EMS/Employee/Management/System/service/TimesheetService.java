package com.EMS.Employee.Management.System.service;

import com.EMS.Employee.Management.System.dto.TimesheetDTO;
import java.util.List;

import org.springframework.web.bind.annotation.PathVariable;

public interface TimesheetService {
    TimesheetDTO create(int employeeId, TimesheetDTO dto);
    List<TimesheetDTO> getTimesheetsForAdmin(Long adminUserId);
    List<TimesheetDTO> getByEmployeeId(int employeeId);
    List<TimesheetDTO> getAllTimesheets();
    void deleteTimesheet(Long id);
    TimesheetDTO updateStatus(Long id, String status);
    TimesheetDTO updateTimesheet(Long id, TimesheetDTO dto);
}