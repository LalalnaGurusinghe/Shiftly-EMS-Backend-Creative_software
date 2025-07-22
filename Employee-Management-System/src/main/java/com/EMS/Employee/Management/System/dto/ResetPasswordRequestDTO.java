package com.EMS.Employee.Management.System.dto;

import lombok.Data;

@Data
public class ResetPasswordRequestDTO {
    private String token;
    private String newPassword;
    private String confirmPassword;
} 