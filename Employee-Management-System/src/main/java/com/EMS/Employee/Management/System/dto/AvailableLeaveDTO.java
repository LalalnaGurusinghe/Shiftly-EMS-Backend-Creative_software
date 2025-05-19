package com.EMS.Employee.Management.System.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AvailableLeaveDTO {
    private String type;
    private int count;
    private String hexColor;
}
