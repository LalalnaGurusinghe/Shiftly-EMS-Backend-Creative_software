package com.EMS.Employee.Management.System.dto;

import lombok.Data;

@Data
public class ReferCandidateDTO {
    private Long id;
    private int employeeId;
    private String vacancyName;
    private String applicantName;
    private String applicantEmail;
    private String message;
    private String fileUrl;
    private String status;
    private String departmentName;
    private String employeeName;
}