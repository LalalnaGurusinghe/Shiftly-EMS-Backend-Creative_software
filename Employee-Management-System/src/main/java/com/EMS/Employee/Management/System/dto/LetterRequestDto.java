package com.EMS.Employee.Management.System.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LetterRequestDto {
    private Long id;
    private String letterType;
    private String status;

    private String generatedLetterHtml;
    private Map<String, Object> fields;
    private int employeeId;
    private String departmentName;
    private String employeeName;
}
