package com.EMS.Employee.Management.System.service;

import com.EMS.Employee.Management.System.dto.CheckLeaveDTO;
import com.EMS.Employee.Management.System.entity.LeaveStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface CheckLeaveService {
    List<CheckLeaveDTO> getAll();
    ResponseEntity<CheckLeaveDTO> getLeaveById(int id);
    ResponseEntity<CheckLeaveDTO> updateLeaveStatus(int id, LeaveStatus leaveStatus, String adminEmail);
}