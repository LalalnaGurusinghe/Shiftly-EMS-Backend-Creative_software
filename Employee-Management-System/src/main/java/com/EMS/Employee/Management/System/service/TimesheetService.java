package com.EMS.Employee.Management.System.service;

import com.EMS.Employee.Management.System.dto.TimesheetDTO;
import java.util.List;

public interface TimesheetService {
    TimesheetDTO submitTimesheet(TimesheetDTO dto, String username);
    List<TimesheetDTO> getUserTimesheets(String username);
    List<TimesheetDTO> getAllTimesheets();
    TimesheetDTO approveTimesheet(Long id, String adminUsername);
    TimesheetDTO rejectTimesheet(Long id, String adminUsername);
} 