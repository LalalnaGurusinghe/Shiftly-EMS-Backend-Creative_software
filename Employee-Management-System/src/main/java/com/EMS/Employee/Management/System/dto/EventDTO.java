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
    private String title;
    private String eventType;
    private LocalDate enableDate;
    private LocalDate expireDate;
    private String status;
    private String imageUrl;
    private Long userId;
}