package com.EMS.Employee.Management.System.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;
import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonFormat;

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
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdAt;
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