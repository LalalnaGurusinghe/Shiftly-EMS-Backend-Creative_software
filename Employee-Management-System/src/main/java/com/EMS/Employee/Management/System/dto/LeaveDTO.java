package com.EMS.Employee.Management.System.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class LeaveDTO {
    private Long id;
    private Long userId;
    private String username;
    private String leaveType;
    private LocalDate leaveFrom;
    private LocalDate leaveTo;
    private Integer duration;
    private Integer coverPersonId;
    private String coverPersonName;
    private Integer reportToId;
    private String reportToName;
    private String reason;
    private String leaveStatus;
    private String fileName;
    private String filePath;
} 