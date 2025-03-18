package com.EMS.Employee.Management.System.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventDTO {

    private Long id;
    private String title;
    private String formUrl;
    private String responseUrl;
    private String audience;
    private String eventType;
    private String projects;

    private LocalDateTime enableDateTime;
    private LocalDateTime expireDateTime;

    private String createdBy;

    private byte[] photo;
}
