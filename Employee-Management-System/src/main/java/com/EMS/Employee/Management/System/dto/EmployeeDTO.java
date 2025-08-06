package com.EMS.Employee.Management.System.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeDTO {
    private int employeeId;
    private Long userId;
    private String firstName;
    private String lastName;
    private String gender;
    private String dob;
    private String location;
    private String designation;
    private String department;
    private Long departmentId;
    private String reportingPerson;
    private Long reportingPersonId;
    private String reportingPersonEmail;
    private java.util.List<String> skills;
    private java.util.List<String> education;
    private java.util.List<String> experience;
    private String teamName;
}