package com.EMS.Employee.Management.System.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    private Long id;
    private String username;
    private String email;
    private Set<String> roles;
    private boolean isActive;
    private boolean isVerified;
    private String designation;
    private String department;
    private String createdAt;
    private String reportingPerson;
    private String reportingPersonEmail;

    public String getReportingPerson() {
        return reportingPerson;
    }
    public void setReportingPerson(String reportingPerson) {
        this.reportingPerson = reportingPerson;
    }
    public String getReportingPersonEmail() {
        return reportingPersonEmail;
    }
    public void setReportingPersonEmail(String reportingPersonEmail) {
        this.reportingPersonEmail = reportingPersonEmail;
    }
}