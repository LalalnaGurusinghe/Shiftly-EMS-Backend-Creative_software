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
    private Long referredById;
    private String referredByUsername;
    private String status;
} 