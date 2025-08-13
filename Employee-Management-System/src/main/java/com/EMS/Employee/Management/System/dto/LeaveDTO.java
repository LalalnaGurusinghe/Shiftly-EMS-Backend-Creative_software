package com.EMS.Employee.Management.System.dto;

import lombok.Data;

@Data
public class LeaveDTO {
    private Long id;
    private int employeeId;
    private String employeeName;
    private String leaveType;
    private String leaveFrom;
    private String leaveTo;
    private int coverPersonId;
    private String coverPersonName;
    private String departmentName;
    private String reason;
    private String status;
} 