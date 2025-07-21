package com.EMS.Employee.Management.System.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class ProjectDTO {
    private Long projectId;
    private String name;
    private String description;
    private Long teamId;
    private String teamName;
    private LocalDate startDate;
    private LocalDate endDate;
    private Long departmentId;
    private String departmentName;
    private int progress;
} 