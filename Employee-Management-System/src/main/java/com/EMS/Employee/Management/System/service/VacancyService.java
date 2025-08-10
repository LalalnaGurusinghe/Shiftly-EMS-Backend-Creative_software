package com.EMS.Employee.Management.System.service;

import com.EMS.Employee.Management.System.dto.VacancyDTO;
import java.util.List;

public interface VacancyService {
    VacancyDTO createVacancy(VacancyDTO vacancyDTO);
    VacancyDTO updateVacancy(Long id, VacancyDTO dto);
    void deleteVacancy(Long id);
    List<VacancyDTO> getAllVacancies();
    List<VacancyDTO> getVacancyByDepartmentId(Long departmentId);
    // VacancyDTO getVacancyById(Long id);
} 