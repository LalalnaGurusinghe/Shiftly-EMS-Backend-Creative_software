package com.EMS.Employee.Management.System.dto;

import lombok.Data;

@Data
public class ClaimDTO {
    private Long id;
    private String claimType;
    private String description;
    private String fileName;
    private String filePath;
    private String status;
    private Long requestedById;
    private String requestedByUsername;
} 