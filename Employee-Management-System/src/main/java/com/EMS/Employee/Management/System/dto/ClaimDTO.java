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
    private Long userId;
    // fileData is a Base64 string for API, but is stored as byte[] in the entity
    private String fileData;
} 