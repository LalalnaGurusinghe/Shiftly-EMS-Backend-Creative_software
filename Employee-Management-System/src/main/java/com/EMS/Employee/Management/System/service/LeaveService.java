package com.EMS.Employee.Management.System.service;

import com.EMS.Employee.Management.System.dto.LeaveDTO;
import java.util.List;

public interface LeaveService {
    // Employee
    // LeaveDTO applyLeave(LeaveDTO leaveDTO, String username);
    List<LeaveDTO> getOwnLeaves(String username);
    LeaveDTO updateOwnLeave(Long leaveId, LeaveDTO leaveDTO, String username);
    void deleteOwnLeave(Long leaveId, String username);

    // Admin
    List<LeaveDTO> getAllLeaves();
    LeaveDTO updateLeaveStatus(Long leaveId, String status);
} 