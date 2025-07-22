package com.EMS.Employee.Management.System.dto;

import lombok.Data;

@Data
public class ReferCandidateDTO {
    private Long id;
    private String vacancyName;
    private String applicantName;
    private String applicantEmail;
    private String message;
    // resumeData is a Base64 string for API, but is stored as byte[] in the entity
    private String resumeData;
    private String status;
    private Long userId;
} 