package com.EMS.Employee.Management.System.dto;

import lombok.Data;

@Data
public class ReferCandidateDTO {
    private Long id;
    private Long vacancyId;
    private String vacancyName;
    private String applicantName;
    private String applicantEmail;
    private String message;
    private String resumeFileName;
    private String resumeFilePath;
    // resumeData is a Base64 string for API, but is stored as byte[] in the entity
    private String resumeData;
    private Long referredById;
    private String referredByUsername;
    private Long referredByUserId;
    private String referredByFirstName;
    private String status;
    private Long userId;
} 