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
    private Long userId; // Reference to User entity
    private String leaveType;
    private java.time.LocalDate leaveFrom;
    private java.time.LocalDate leaveTo;
    private int duration;
    private String coverPerson;
    private String reportTo;
    private String reason;
    private com.EMS.Employee.Management.System.entity.LeaveStatus status;
    private String createdBy;
    private String updatedBy;
    private java.time.LocalDateTime createdAt;
    private java.time.LocalDateTime updatedAt;
}