package com.EMS.Employee.Management.System.dto;

import com.EMS.Employee.Management.System.entity.TimesheetStatus;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class TimesheetDTO {
    private Long id;
    private Long userId;
    private String username;
    private LocalDate date;
    private Double hoursWorked;
    private String description;
    private TimesheetStatus status;
    private LocalDateTime submittedAt;
    private LocalDateTime approvedAt;
} 