package com.EMS.Employee.Management.System.dto;

import com.EMS.Employee.Management.System.entity.LeaveStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CheckLeaveDTO {
    private int checkId;
    private int leaveId;
    private LeaveStatus status;
    private Long adminId; // Updated to Long
    private String adminName;
    private RequestLeaveDTO requestLeaveDTO;
}