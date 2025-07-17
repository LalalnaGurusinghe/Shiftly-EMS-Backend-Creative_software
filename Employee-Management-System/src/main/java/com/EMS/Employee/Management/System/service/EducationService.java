package com.EMS.Employee.Management.System.service;

import com.EMS.Employee.Management.System.dto.EducationDTO;
import java.util.List;

public interface EducationService {
    EducationDTO addEducation(EducationDTO educationDTO);
    EducationDTO updateEducation(Long id, EducationDTO educationDTO);
    void deleteEducation(Long id);
    List<EducationDTO> getEducationByEmployeeId(Integer employeeId);
    EducationDTO getEducationById(Long id);
} 