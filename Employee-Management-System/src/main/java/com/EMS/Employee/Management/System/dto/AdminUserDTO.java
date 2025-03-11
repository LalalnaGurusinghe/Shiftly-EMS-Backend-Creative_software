package com.EMS.Employee.Management.System.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminUserDTO {

    private int employeeNo;
    private String firstName;
    private String lastName;
    private String gender;
    private String birthOfDate;
    private String location;
    private String email;
    private int epfNO;
    private String designation;
    private String department;
    private String reportingPerson;
}
