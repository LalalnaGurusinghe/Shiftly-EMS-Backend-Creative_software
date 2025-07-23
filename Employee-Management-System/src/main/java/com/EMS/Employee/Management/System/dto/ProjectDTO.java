package com.EMS.Employee.Management.System.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class ProjectDTO {
    private Long projectId;
    private String name;
    private String description;
    private String teamName;
    private Long teamId;
    private LocalDate startDate;
    private LocalDate endDate;
    private String departmentName;
    private Long departmentId;
    private int progress;
    private Long userId;
} 