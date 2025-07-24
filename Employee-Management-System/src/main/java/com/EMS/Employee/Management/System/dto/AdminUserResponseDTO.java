package com.EMS.Employee.Management.System.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminUserResponseDTO {
    private String firstName;
    private String lastName;
    private String email;
}
