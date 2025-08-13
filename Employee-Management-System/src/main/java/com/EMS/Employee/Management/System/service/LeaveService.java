package com.EMS.Employee.Management.System.service;

import com.EMS.Employee.Management.System.dto.LeaveDTO;

import java.util.List;

public interface LeaveService {
    LeaveDTO applyLeave(int employeeId, LeaveDTO dto);
    List<LeaveDTO> getLeavesForAdmin(Long adminUserId);
    List<LeaveDTO> getByEmployeeId(int employeeId);
    List<LeaveDTO> getAllLeaves();
    void deleteLeave(Long id);
    LeaveDTO updateStatus(Long id, String status);
    LeaveDTO updateLeave(Long id, LeaveDTO dto);
} 