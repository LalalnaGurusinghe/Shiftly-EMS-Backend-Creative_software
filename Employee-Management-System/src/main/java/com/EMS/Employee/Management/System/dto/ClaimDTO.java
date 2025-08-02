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
    private String claimType;
    private String description;
    private String status;
    private Long userId;
    private String claimUrl;
    private LocalDate claimDate;
}