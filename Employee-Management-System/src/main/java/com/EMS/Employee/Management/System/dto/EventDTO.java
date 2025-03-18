package com.EMS.Employee.Management.System.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
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
    private String formUrl;
    private String responseUrl;
    private String audience;
    private String eventType;
    private String projects;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate enableDate;
    private LocalDate expireDate;


}
