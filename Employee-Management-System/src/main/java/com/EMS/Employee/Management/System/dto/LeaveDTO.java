package com.EMS.Employee.Management.System.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class LeaveDTO {
    private Long id;
    private Long userId;

   
    private String leaveType;
    private LocalDate leaveFrom;
    private LocalDate leaveTo;
   
    
    private String coverPersonName;
    private String departmentName;
   
    private String reportToName;
    private String reason;
    private String leaveStatus;
    
} 