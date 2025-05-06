package com.EMS.Employee.Management.System.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminUserDTO {
    private int employeeNo;
    @NotBlank(message = "First name is required")
    private String firstName;
    @NotBlank(message = "Last name is required")
    private String lastName;
    @NotBlank(message = "Gender is required")
    private String gender;
    @NotBlank(message = "Date of birth is required")
    private String birthOfDate;
    @NotBlank(message = "Location is required")
    private String location;
    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;
    private int epfNo;
    @NotBlank(message = "Designation is required")
    private String designation;
    @NotBlank(message = "Department is required")
    private String department;
    @NotBlank(message = "Reporting person is required")
    private String reportingPerson;
}