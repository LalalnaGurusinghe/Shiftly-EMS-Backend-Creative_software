package com.EMS.Employee.Management.System.dto;

public record RoleDTO(String role) {
    public RoleDTO {
        if (role != null) {
            role = role.toUpperCase();
        }
    }
}