package com.EMS.Employee.Management.System.dto;

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
    private String skills;
    private String education;
    private String username;
}