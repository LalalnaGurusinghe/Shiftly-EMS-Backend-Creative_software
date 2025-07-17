package com.EMS.Employee.Management.System.dto;

import lombok.Data;

@Data
public class TeamMemberDTO {
    private Long id;
    private Long teamId;
    private Integer employeeId;
    private String employeeName;
} 