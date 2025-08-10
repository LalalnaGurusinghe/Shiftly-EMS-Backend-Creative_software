package com.EMS.Employee.Management.System.dto;

import lombok.Data;

@Data
public class VacancyDTO {
    private Long id;
    private Long departmentId;
    private String departmentName;
    private String vacancyName;
} 