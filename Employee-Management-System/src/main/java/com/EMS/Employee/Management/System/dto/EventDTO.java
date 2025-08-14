package com.EMS.Employee.Management.System.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventDTO {
    private Long id;
    private int employeeId;
    private String title;
    private String eventType;
    private String enableDate;
    private String expireDate;
    private String status;
    private String imageUrl;
    private String departmentName;
    private String employeeName;
}