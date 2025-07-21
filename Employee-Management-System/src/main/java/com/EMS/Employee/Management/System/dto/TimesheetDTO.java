package com.EMS.Employee.Management.System.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class TimesheetDTO {
    private Long timesheetId;
    private Integer employeeId;
    private Long projectId;
    private String projectName;
    private LocalDate date;
    private Double workingHours;
    private String activities;
    private String status;
    private Long userId;
    private String employeeFirstName;
} 