package com.EMS.Employee.Management.System.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReferralDTO {
    private Long id;
    private String vacancy;
    private String applicantName;
    private String applicantEmail;
    private String message;
    private String resumeFileName;
}
