package com.EMS.Employee.Management.System.service;

import com.EMS.Employee.Management.System.dto.VacancyDTO;
import java.util.List;

public interface VacancyService {
    VacancyDTO addVacancy(VacancyDTO vacancyDTO);
    VacancyDTO updateVacancy(Long id, VacancyDTO vacancyDTO);
    void deleteVacancy(Long id);
    List<VacancyDTO> getAllVacancies();
    VacancyDTO getVacancyById(Long id);
} 