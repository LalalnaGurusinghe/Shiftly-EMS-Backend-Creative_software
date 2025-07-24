package com.EMS.Employee.Management.System.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClaimDTO {
    private Long id;
    private String claimType;
    private String description;
    private String status;
    private Long userId;
    private String claimUrl;
    private LocalDate claimDate;
    // Add these new fields
    private String employeeName;
    private String employeeEmail;
    private String department;
}