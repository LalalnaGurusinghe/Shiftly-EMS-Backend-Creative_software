package com.EMS.Employee.Management.System.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminUserDTO {

    private int employeeNo;

    @NotBlank(message = "First name is required")
    @Size(max = 50, message = "First name must be 50 characters or less")
    private String firstName;

    @NotBlank(message = "Last name is required")
    @Size(max = 50, message = "Last name must be 50 characters or less")
    private String lastName;

    @NotBlank(message = "Gender is required")
    @Pattern(regexp = "^(Male|Female|Other)$", message = "Gender must be Male, Female, or Other")
    private String gender;

    @NotBlank(message = "Date of birth is required")
    @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$", message = "Date of birth must be in YYYY-MM-DD format")
    private String birthOfDate;

    @NotBlank(message = "Location is required")
    @Size(max = 100, message = "Location must be 100 characters or less")
    private String location;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    private int epfNo;

    @NotBlank(message = "Designation is required")
    @Size(max = 50, message = "Designation must be 50 characters or less")
    private String designation;

    @NotBlank(message = "Department is required")
    @Size(max = 50, message = "Department must be 50 characters or less")
    private String department;

    @NotBlank(message = "Reporting person is required")
    @Size(max = 100, message = "Reporting person must be 100 characters or less")
    private String reportingPerson;
}