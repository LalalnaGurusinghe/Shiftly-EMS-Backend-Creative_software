package com.EMS.Employee.Management.System.dto;

import com.EMS.Employee.Management.System.entity.LeaveStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestLeaveDTO {
    private Integer leaveId;
    @NotNull(message = "User ID is required")
    private Long userId;
    @NotBlank(message = "User name is required")
    private String userName;
    @NotBlank(message = "Leave type is required")
    private String leaveType;
    @NotBlank(message = "Leave from date is required")
    private String leaveFrom;
    @NotBlank(message = "Leave to date is required")
    private String leaveTo;
    private int duration;
    @NotBlank(message = "Cover person is required")
    private String coverPerson;
    @NotBlank(message = "Report to is required")
    private String reportTo;
    @NotBlank(message = "Reason is required")
    private String reason;
    private LeaveStatus status;
}