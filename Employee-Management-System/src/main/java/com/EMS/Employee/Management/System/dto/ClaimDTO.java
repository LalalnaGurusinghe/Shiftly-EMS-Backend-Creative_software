package com.EMS.Employee.Management.System.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClaimDTO {
    private Long id;
    private int employeeId;
    private String claimType;
    private String description;
    private String status;
    private String claimUrl;
    private String claimDate;
    private String departmentName;
    private String employeeName;
}