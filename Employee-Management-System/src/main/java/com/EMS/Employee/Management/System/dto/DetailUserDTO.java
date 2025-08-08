package com.EMS.Employee.Management.System.dto;

import lombok.Data;
import java.util.List;

@Data
public class DetailUserDTO {
    private Long id;
    private String username;
    private String email;
    private List<String> roles;

    private Long designationId;
    private String designationName;
    private String department;
    private Long departmentId;
    private String reportingPerson;
    private Long reportingPersonId;
    private String reportingPersonEmail;
}