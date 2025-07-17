package com.EMS.Employee.Management.System.service;

import com.EMS.Employee.Management.System.dto.TimesheetDTO;
import java.util.List;

public interface TimesheetService {
    // Employee
    TimesheetDTO submitTimesheet(TimesheetDTO dto, String username);
    List<TimesheetDTO> getOwnTimesheets(String username);

    // Admin
    List<TimesheetDTO> getAllTimesheets();
    TimesheetDTO approveTimesheet(Long timesheetId);
    TimesheetDTO rejectTimesheet(Long timesheetId);
} 