package com.EMS.Employee.Management.System.dto;

import lombok.Data;

@Data
public class TeamDTO {
    private Long teamId;
    private String name;
    private Long departmentId;
    private String departmentName;
} 