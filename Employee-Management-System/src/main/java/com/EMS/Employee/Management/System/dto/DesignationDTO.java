package com.EMS.Employee.Management.System.dto;

import lombok.Data;

@Data
public class DesignationDTO {
    private Long id;
    private String designationName;
    private Long departmentId;
    private String departmentName;
}