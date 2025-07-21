package com.EMS.Employee.Management.System.dto;

import lombok.Data;

@Data
public class VerifyUserRequestDTO {
    private String role;
    private String department;
    private String designation;

    public String getDesignation() {
        return designation;
    }
} 