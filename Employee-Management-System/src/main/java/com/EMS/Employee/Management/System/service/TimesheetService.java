package com.EMS.Employee.Management.System.service;

import com.EMS.Employee.Management.System.dto.TimesheetDTO;
import java.util.List;

public interface TimesheetService {
    TimesheetDTO createTimesheet(TimesheetDTO dto);
    List<TimesheetDTO> getAllTimesheets();
    List<TimesheetDTO> getTimesheetsByUserId(Long userId);
    void deleteTimesheet(Long id, Long userId);
    TimesheetDTO updateStatus(Long id, String status);
} 