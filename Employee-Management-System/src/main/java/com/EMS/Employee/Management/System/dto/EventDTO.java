package com.EMS.Employee.Management.System.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventDTO {
    private Long id;
    @NotBlank(message = "Title is required")
    private String title;
    @NotBlank(message = "Form URL is required")
    private String formUrl;
    @NotBlank(message = "Response URL is required")
    private String responseUrl;
    @NotBlank(message = "Audience is required")
    private String audience;
    @NotBlank(message = "Event type is required")
    private String eventType;
    @NotBlank(message = "Projects are required")
    private String projects;
    @NotNull(message = "Enable date and time are required")
    private LocalDateTime enableDateTime;
    @NotNull(message = "Expire date and time are required")
    private LocalDateTime expireDateTime;
    @NotBlank(message = "Created by is required")
    private String createdBy;
    private byte[] photo;
}