package com.EMS.Employee.Management.System.dto;

import com.EMS.Employee.Management.System.entity.LeaveStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestLeaveDTO {

    private int leaveId;
    private int userId;
    private String userName;
    private String leaveType;
    private String leaveFrom;
    private String leaveTo;
    private int duration;
    private String coverPerson;
    private String reportTo;
    private String reason;
    private LeaveStatus status;
}

