package com.EMS.Employee.Management.System.dto;

import lombok.Data;

@Data
public class TimesheetDTO {
    private Long id;
    private int employeeId;
    private String date;
    private String mode;
    private String activity;
    private double hours;
    private String status;
    private String departmentName;
    private String employeeName;
} 